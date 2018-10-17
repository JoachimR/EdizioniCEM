package de.reiss.edizioni.audio.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build


const val NOTIFICATION_PLAYER_CHANNEL_ID = "EdizioniCEM"
private const val NOTIFICATION_PLAYER_CHANNEL_NAME = "Audio Player"

fun createNotificationChannelForPlayer(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NotificationHelper.notificationManager(context).createNotificationChannel(
                NotificationChannel(NOTIFICATION_PLAYER_CHANNEL_ID,
                        NOTIFICATION_PLAYER_CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_LOW).apply {
                    description = "Audio Playback Controls"
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC;
                    setShowBadge(false)
                })
    }
}
