package com.student.Compass_Abroad.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Html
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.App.AppState
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.activities.MainActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {

    @SuppressLint("SuspiciousIndentation")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("PushNotification", "Notification received")

        val data = remoteMessage.data
        Log.d("PushNotification", "Notification data: $data")

        val moduleType = data["module_type"]
        val moduleId = data["module_id"]
        val clientNumber = data["client_number"]
        val notificationId = data["notification_id"]
        val application_identifier = data["application_identifier"]
        val lead_identifier = data["lead_identifier"]
        val ambassador_conversation_identifier = data["ambassador_conversation_identifier"]

        if (!AppState.isInChatScreen) {
            remoteMessage.notification?.let {
                Log.d("PushNotification", "Showing notification: Title=${it.title}, Body=${it.body}")
                showNotification(
                    it.title,
                    it.body,
                    moduleType,
                    moduleId,
                    clientNumber,
                    notificationId,
                    application_identifier,
                    lead_identifier,
                    ambassador_conversation_identifier
                )
            } ?: run {
                Log.d("PushNotification", "remoteMessage.notification is null")
            }
        } else {
            Log.d("PushNotification", "User is in chat screen, skipping notification")
        }
    }

    private fun showNotification(
        title: String?, body: String?,
        moduleType: String?, moduleId: String?, clientNumber: String?, notificationId: String?, application_identifier: String?, lead_identifier: String?, ambassador_conversation_identifier: String?
    ) {
        Log.d("PushNotification", "Preparing to show notification")

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "YOUR_CHANNEL_ID",
                "Channel Name",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
            Log.d("PushNotification", "Notification channel created")
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP

            putExtra("from_notification", true)
            putExtra("skip_splash", true)
            putExtra("module_type", moduleType)
            putExtra("module_id", moduleId)
            putExtra("client_number", clientNumber)
            putExtra("application_identifier", application_identifier)
            putExtra("lead_identifier", lead_identifier)
            putExtra("ambassador_conversation_identifier", ambassador_conversation_identifier)

            action = "NOTIFICATION_ACTION_${System.currentTimeMillis()}"
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val formattedBody = Html.fromHtml(body ?: "", Html.FROM_HTML_MODE_LEGACY)

        val notificationBuilder = NotificationCompat.Builder(this, "YOUR_CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(formattedBody)
            .setStyle(NotificationCompat.BigTextStyle().bigText(formattedBody))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notifyId = notificationId?.toIntOrNull() ?: 0
        Log.d("PushNotification", "Notifying with ID $notifyId")
        notificationManager.notify(notifyId, notificationBuilder.build())
    }
}
