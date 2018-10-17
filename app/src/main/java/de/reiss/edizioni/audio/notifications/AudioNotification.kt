package de.reiss.edizioni.audio.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import android.support.v4.media.app.NotificationCompat.MediaStyle
import android.support.v4.media.session.MediaButtonReceiver.buildMediaButtonPendingIntent
import android.support.v4.media.session.PlaybackStateCompat
import de.reiss.edizioni.R
import de.reiss.edizioni.audio.AudioStream
import de.reiss.edizioni.audio.AudioStreamPlayerService
import de.reiss.edizioni.main.MainActivity
import java.util.*

fun startAudioForegroundNotification(service: AudioStreamPlayerService,
                                     audioStream: AudioStream) {

    NotificationHelper.startForeground(service, NotificationCategory.Audio,

            createBuilder(service)

                    .addAction(R.drawable.ic_pause,
                            service.getText(R.string.notification_button_pause_title),
                            StartPlayingReceiver.createPendingIntent(service, audioStream))

                    .build())
}

fun showAudioPausedNotification(context: Context,
                                audioStream: AudioStream) {

    NotificationHelper.show(context, NotificationCategory.Audio,

            createBuilder(context)
                    .addAction(R.drawable.ic_play,
                            context.getText(R.string.notification_button_play_title),
                            StartPlayingReceiver.createPendingIntent(context, audioStream))
            .build())
}

fun cancelPausedNotification(context: Context) {
    NotificationHelper.cancel(context, NotificationCategory.Audio)
}

private fun createBuilder(context: Context)
        : NotificationCompat.Builder {

    createNotificationChannelForPlayer(context)

    return NotificationCompat.Builder(context, NOTIFICATION_PLAYER_CHANNEL_ID)
            .setStyle(MediaStyle()
                    .setShowActionsInCompactView(0)
                    .setShowCancelButton(true)
                    .setCancelButtonIntent(buildMediaButtonPendingIntent(
                            context, PlaybackStateCompat.ACTION_STOP)))
            .setCategory(NotificationCompat.CATEGORY_TRANSPORT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setShowWhen(false)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(contentPendingIntent(context))
            .setContentTitle(context.getString(R.string.app_name))
            .setSmallIcon(R.drawable.ic_play)
            .setWhen(0)
            .setAutoCancel(true)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))

            as NotificationCompat.Builder
}

private fun contentPendingIntent(context: Context) =
        PendingIntent.getActivity(context, Random().nextInt(100),
                MainActivity.createIntent(context)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP),
                PendingIntent.FLAG_UPDATE_CURRENT)
