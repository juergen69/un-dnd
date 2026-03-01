package com.smsdndmanager.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.smsdndmanager.R
import com.smsdndmanager.presentation.screen.MainActivity

/**
 * Helper class for showing notifications when DND actions are performed
 */
class DndActionNotificationHelper(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "dnd_actions"
        private const val CHANNEL_NAME = "DND Actions"
        private const val NOTIFICATION_ID = 1001
        
        private const val PERSISTENT_CHANNEL_ID = "dnd_service"
        private const val PERSISTENT_CHANNEL_NAME = "DND Service"
        private const val PERSISTENT_NOTIFICATION_ID = 1002
    }

    init {
        createNotificationChannel()
        createPersistentNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications when authorized contacts change DND settings"
                setShowBadge(true)
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createPersistentNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                PERSISTENT_CHANNEL_ID,
                PERSISTENT_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Persistent notification showing service is running"
                setShowBadge(false)
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Show a persistent notification indicating the service is running
     */
    fun showPersistentNotification() {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, PERSISTENT_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("SMS DND Manager")
            .setContentText("Listening for authorized SMS commands")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setOngoing(true) // Make notification persistent (can't be swiped away)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(PERSISTENT_NOTIFICATION_ID, notification)
    }

    /**
     * Show a notification that DND was disabled by an authorized contact
     */
    fun showDndDisabledNotification(contactName: String, volumePercent: Int) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("DND Disabled")
            .setContentText("$contactName un-DND'ed the phone and set volume to $volumePercent%")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("$contactName un-DND'ed the phone and set the volume to $volumePercent%"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .build()

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    /**
     * Dismiss the DND action notification
     */
    fun dismissNotification() {
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.cancel(NOTIFICATION_ID)
    }

    /**
     * Dismiss the persistent notification
     */
    fun dismissPersistentNotification() {
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.cancel(PERSISTENT_NOTIFICATION_ID)
    }
}