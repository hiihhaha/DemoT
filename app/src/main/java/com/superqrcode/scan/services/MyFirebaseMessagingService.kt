package com.superqrcode.scan.services

import com.google.firebase.messaging.FirebaseMessagingService
import android.content.ContentValues
import com.google.firebase.messaging.RemoteMessage
import android.content.Intent
import android.media.RingtoneManager
import com.superqrcode.scan.R
import android.app.PendingIntent
import android.app.NotificationManager
import android.os.Build
import android.app.NotificationChannel
import android.util.Log
import androidx.core.app.NotificationCompat
import com.superqrcode.scan.view.activity.MainActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.d(ContentValues.TAG, "Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // TODO(developer): Handle FCM messages here.
        super.onMessageReceived(remoteMessage)
        Log.d(ContentValues.TAG, "From: " + remoteMessage.from)

        // Check if message contains a data payload.
        if (remoteMessage.data.size > 0) {
            Log.d(ContentValues.TAG, "Message data payload: " + remoteMessage.data)
        }
        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            val intentAct = Intent(this, MainActivity::class.java)
            intentAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            Log.d(
                ContentValues.TAG, "Message Notification Body: " + remoteMessage.notification!!
                    .body
            )
            val notificationBuilder = NotificationCompat.Builder(this, "channel_id")
                .setContentTitle(remoteMessage.notification!!.title)
                .setContentText(remoteMessage.notification!!.body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(NotificationCompat.BigTextStyle())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(PendingIntent.getActivity(this, 0, intentAct, 0))
                .setAutoCancel(true)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    "notification_channel002",
                    "Notification FCM",
                    NotificationManager.IMPORTANCE_LOW
                )
                notificationManager.createNotificationChannel(channel)
            }
            notificationManager.notify(0, notificationBuilder.build())
        }
    }

    companion object {
        private const val NOTIFICATIOON_CHECK_FILE_SEEN = "NOTIFICATIOON_CHECK_FILE_SEEN"
    }
}