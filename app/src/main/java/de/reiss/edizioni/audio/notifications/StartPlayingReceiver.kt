package de.reiss.edizioni.audio.notifications

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import de.reiss.edizioni.audio.AudioStream
import de.reiss.edizioni.audio.AudioStreamPlayerService
import java.util.*

class StartPlayingReceiver : BroadcastReceiver() {

    companion object {

        private const val KEY_STREAM = "KEY_STREAM"

        fun createPendingIntent(context: Context, audioStream: AudioStream): PendingIntent {
            return PendingIntent.getBroadcast(context, uniqueRequestCode(),
                    createIntent(context, audioStream), FLAG_UPDATE_CURRENT)
        }

        private fun uniqueRequestCode() = Random().nextInt(100)

        private fun createIntent(context: Context, audioStream: AudioStream) =
                Intent(context, StartPlayingReceiver::class.java)
                        .putExtra(KEY_STREAM, audioStream)
    }

    override fun onReceive(context: Context, intent: Intent) {
        val stream = intent.getParcelableExtra<AudioStream?>(KEY_STREAM)
        AudioStreamPlayerService.pauseOrUnpauseCurrentStream()
    }

}
