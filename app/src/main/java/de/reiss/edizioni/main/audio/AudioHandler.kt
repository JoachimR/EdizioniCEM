package de.reiss.edizioni.main.audio

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.Player.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import de.reiss.edizioni.R
import javax.inject.Inject


class AudioHandler @Inject constructor(private val context: Context) {

    private var exoPlayer: SimpleExoPlayer? = null

    private var isPlaying = false

    //"http://www.radiorisposta.org/wp/public/FilesAudio/UPO/UNA%20PAROLA%20PER%20OGGI.mp3"
    fun prepare(url: String) {
        prepareExoPlayerFromURL(url)
    }

    private fun handlePlayerStateChanged(playbackState: Int) {
        when (playbackState) {
            STATE_ENDED -> {
                setPlayPause(false)
                exoPlayer?.seekTo(0)
            }
            STATE_READY -> {
            }
            STATE_BUFFERING -> {
            }
            STATE_IDLE -> {
            }
        }
    }

    private fun prepareExoPlayerFromURL(url: String) {
        val uri = Uri.parse(url)

        val trackSelector = DefaultTrackSelector()

        val loadControl = DefaultLoadControl()
        exoPlayer = ExoPlayerFactory.newSimpleInstance(DefaultRenderersFactory(context), trackSelector, loadControl)
        exoPlayer?.let { player ->
            val dataSourceFactory = DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, context.getString(R.string.app_name)), null)
            val audioSource = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
            player.addListener(object : Player.EventListener {
                override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
                }

                override fun onSeekProcessed() {
                }

                override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
                }

                override fun onPlayerError(error: ExoPlaybackException?) {
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
                    handlePlayerStateChanged(playbackState)
                }

            })
            player.prepare(audioSource)
        }
    }

    private fun setPlayPause(play: Boolean) {
        exoPlayer?.let {
            isPlaying = play
            it.playWhenReady = play
        }
    }

}