package de.reiss.edizioni.audio

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.AudioManager.*
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.widget.Toast
import de.reiss.edizioni.R
import de.reiss.edizioni.audio.notifications.cancelPausedNotification
import de.reiss.edizioni.audio.notifications.showAudioPausedNotification
import de.reiss.edizioni.audio.notifications.startAudioForegroundNotification
import de.reiss.edizioni.events.AudioFilePlaybackChanged
import de.reiss.edizioni.events.AudioStreamProgress
import de.reiss.edizioni.events.postMessageEvent
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit.MILLISECONDS

class AudioStreamPlayerService : Service() {

    companion object {

        var instance: AudioStreamPlayerService? = null

        fun startStream(audioStream: AudioStream) {
            instance?.let {
                if (it.isPlaying(audioStream.url)) {
                    pauseOrUnpauseCurrentStream()
                } else {
                    instance?.start(audioStream)
                }
            }
        }

        fun isPlayingCurrentStream(): Boolean {
            return instance?.isPlayingCurrent() ?: false
        }

        fun pauseOrUnpauseCurrentStream() {
            instance?.pauseOrUnpause()
        }

        fun seekToProgressInCurrentStream(progress: Long) {
            instance?.seekCurrentTo(progress)
        }

        fun stopCurrentStream() {
            instance?.stop()
        }
    }

    private lateinit var audioManager: AudioManager

    private lateinit var playable: AudioStreamPlayer

    private val executor = Executors.newScheduledThreadPool(1)

    private var currentAudioStream: AudioStream? = null

    private val playerEventListener = object : AudioStreamPlayerEventListener {

        override fun onPlaybackChanged(url: String, isNowPlaying: Boolean) {
            postMessageEvent(AudioFilePlaybackChanged(url))
            updateNotification()
        }

        override fun onPlayError() {
            Toast.makeText(this@AudioStreamPlayerService, R.string.error_playing_file, Toast.LENGTH_SHORT).show()
            stopSelf()
        }

    }

    private val focusChangeListener = OnAudioFocusChangeListener { focusChange ->
        if (focusChange == AUDIOFOCUS_LOSS || focusChange == AUDIOFOCUS_LOSS_TRANSIENT) {
            pauseIfCurrentlyPlaying()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAudioFocusRequest() = AudioFocusRequest.Builder(AUDIOFOCUS_GAIN)
            .setAudioAttributes(AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build())
            .setOnAudioFocusChangeListener(focusChangeListener)
            .build()

    private val becomingNoisyReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY == intent?.action) {
                pauseIfCurrentlyPlaying()
            }
        }
    }

    private val localBinder = LocalBinder()

    inner class LocalBinder : Binder() {
        val service: AudioStreamPlayerService
            get() = this@AudioStreamPlayerService
    }

    override fun onCreate() {
        super.onCreate()
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        registerReceiver(becomingNoisyReceiver, IntentFilter(ACTION_AUDIO_BECOMING_NOISY))

        playable = StreamAudioExoPlayer(this, playerEventListener)
    }

    override fun onBind(intent: Intent): IBinder? {
        executor.scheduleWithFixedDelay({
            reportCurrentProgress()
        }, 0, 100, MILLISECONDS)

        return localBinder
    }

    private fun reportCurrentProgress() {
        currentAudioStream?.let {
            if (playable.isPlaying(it.url)) {
                val progress = playable.currentProgress()
                currentAudioStream?.progress = progress
                currentAudioStream?.progress = progress
                postMessageEvent(AudioStreamProgress(it.url, progress, playable.currentDuration()))
            }
        }
    }

    override fun onUnbind(intent: Intent): Boolean {
        executor.shutdown()

        abandonAudioFocus()

        unregisterReceiver(becomingNoisyReceiver)

        currentAudioStream = null
        playable.shutdown()

        return false
    }

    fun seekCurrentTo(progress: Long) {
        playable.seekTo(progress)
    }

    fun pauseIfCurrentlyPlaying() {
        currentAudioStream?.let {
            if (isPlaying(it.url)) {
                playable.pause(it.url)
            }
        }
    }

    fun start(audioStream: AudioStream) {
        if (audioStream.url != currentAudioStream?.url ||
                audioStream.dayPosition != currentAudioStream?.dayPosition) {
            currentAudioStream = audioStream
        }

        currentAudioStream?.let {
            if (!playable.isPlaying(it.url)) {
                doUnpause(it.url, it.progress)
            }
        }
    }

    fun stop() {
        pauseIfCurrentlyPlaying()
        currentAudioStream?.let {
            playable.shutdown()
        }
    }

    fun pauseOrUnpause() {
        currentAudioStream?.let {
            if (playable.isPlaying(it.url)) {
                doPause(it.url)
            } else {
                doUnpause(it.url, it.progress)
            }
        }
    }

    private fun doPause(url: String) {
        playable.pause(url)
    }

    private fun doUnpause(url: String, progress: Long) {
        val audioFocus = requestAudioFocus()
        if (audioFocus == AUDIOFOCUS_REQUEST_GRANTED) {
            playable.play(url, progress)
        }
    }

    private fun requestAudioFocus(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioFocusRequest = getAudioFocusRequest()
            audioManager.requestAudioFocus(audioFocusRequest)
        } else {
            @Suppress("DEPRECATION")
            audioManager.requestAudioFocus(focusChangeListener, STREAM_MUSIC, AUDIOFOCUS_GAIN)
        }
    }

    private fun abandonAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.abandonAudioFocusRequest(getAudioFocusRequest())
        } else {
            @Suppress("DEPRECATION")
            audioManager.abandonAudioFocus(focusChangeListener)
        }
    }

    private fun updateNotification() {
        currentAudioStream.let { stream ->
            if (stream == null) {
                cancelPausedNotification(this)
                stopForeground(true)
            } else {
                if (isPlayingCurrent()) {
                    startAudioForegroundNotification(this, stream)
                } else {
                    stopForeground(false)
                    showAudioPausedNotification(this, stream)
                }
            }
        }
    }

    private fun isPlayingCurrent(): Boolean = currentAudioStream?.let { isPlaying(it.url) } ?: false

    private fun isPlaying(url: String): Boolean = playable.isPlaying(url)

}