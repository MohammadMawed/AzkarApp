package com.mohammadmawed.azkarapp.util

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.mohammadmawed.azkarapp.MainActivity
import com.mohammadmawed.azkarapp.R


// Notification ID.
private const val NOTIFICATION_ID = 0
private val VIBRATION = longArrayOf(1000, 1000, 1000, 1000, 1000)

@RequiresApi(Build.VERSION_CODES.M)
@SuppressLint("UnspecifiedImmutableFlag")
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
    // Create the content intent for the notification, which launches
    // this activity
    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    //add snooze action

    // Build the notification
    val builder = NotificationCompat.Builder(applicationContext, applicationContext.getString(R.string.notification_channel_id))
        //Step 1.8 use the new 'breakfast' notification channel
        .setSmallIcon(R.drawable.ic_notification)
        .setColorized(false)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setVibrate(VIBRATION)
        .setOnlyAlertOnce(true)
        //.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + applicationContext + "/raw/eiskalt.mp3"))
        //add snooze action
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}
