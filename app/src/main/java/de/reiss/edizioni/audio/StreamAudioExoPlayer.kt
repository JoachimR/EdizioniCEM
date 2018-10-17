package de.reiss.edizioni.audio

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import de.reiss.edizioni.R

class StreamAudioExoPlayer(var context: Context,
                           var eventListener: AudioStreamPlayerEventListener) : AudioStreamPlayer {

    private var player: SimpleExoPlayer? = null

    private var currentUrl: String? = null

    override fun play(url: String, progress: Long?) {
        if (currentUrl != url) {
            currentUrl = url
        }
        val uri = Uri.parse(url)

        val dataSourceFactory = DefaultDataSourceFactory(context,
                Util.getUserAgent(context, context.getString(R.string.app_name)), null)
        val audioSource = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri)

        player = ExoPlayerFactory.newSimpleInstance(DefaultRenderersFactory(context),
                DefaultTrackSelector(), DefaultLoadControl())

        player?.addListener(object : Player.EventListener {
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
            }

            override fun onSeekProcessed() {
            }

            override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
            }

            override fun onPlayerError(error: ExoPlaybackException?) {
                eventListener.onPlayError()
            }

            override fun onLoadingChanged(isLoading: Boolean) {
            }

            override fun onPositionDiscontinuity(reason: Int) {
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            }

            override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_ENDED -> {
                        currentUrl?.let {
                            pause(it)
                            player?.seekTo(0)
                        }
                    }
                    Player.STATE_READY -> {
                    }
                    Player.STATE_BUFFERING -> {
                    }
                    Player.STATE_IDLE -> {
                    }
                }

                currentUrl?.let {
                    eventListener.onPlaybackChanged(
                            url = it,
                            isNowPlaying = playWhenReady && playbackState == Player.STATE_READY)
                }

            }

        })
        player?.prepare(audioSource)

        if (progress != null) {
            player?.seekTo(progress.toLong())
        }

        @Suppress("UsePropertyAccessSyntax")
        player?.setPlayWhenReady(true)
    }

    override fun pause(url: String) {
        if (url == currentUrl) {
            @Suppress("UsePropertyAccessSyntax")
            player?.setPlayWhenReady(false)
        }
    }

    override fun isPlaying(url: String): Boolean {
        val setToUrl = isSetToUrl(url)
        val playWhenReady = player?.playWhenReady ?: false
        val stateReady = player?.playbackState == Player.STATE_READY
        return setToUrl && playWhenReady && stateReady
    }

    override fun isSetToUrl(url: String) = url == currentUrl

    override fun shutdown() {
        player?.stop()
        player?.release()
        currentUrl = null
    }

    override fun seekTo(progress: Long) {
        player?.seekTo(progress)
    }

    override fun seekRelative(toCurrentPosition: Long) {
        player?.let { player ->
            val newPosition = player.currentPosition + toCurrentPosition
            player.seekTo(sanitizedPosition(newPosition, player.duration))
        }
    }

    private fun sanitizedPosition(position: Long, duration: Long) =
            when {
                position < 0 -> 0
                position > duration -> duration
                else -> position
            }

    override fun currentProgress() = player?.currentPosition ?: 0

    override fun currentDuration() = player?.duration ?: 0

}