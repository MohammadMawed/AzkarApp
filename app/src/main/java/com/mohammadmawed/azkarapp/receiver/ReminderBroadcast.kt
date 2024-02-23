package com.mohammadmawed.azkarapp.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

import com.mohammadmawed.azkarapp.util.sendNotification


class ReminderBroadcast : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(p0: Context, p1: Intent?) {
        val notificationManager = ContextCompat.getSystemService(p0, NotificationManager::class.java) as NotificationManager

        // Determine the notification type
        val notificationText = when (p1?.getStringExtra("notification_type")) {
            "second" -> "Azkar Almasah Time. It's Azkar Almasah time. Don't forget to do them!"
            else -> "Azkar Alsabah Time. It's Azkar Alsabah time. Don't forget to do them!"
        }

        notificationManager.sendNotification(notificationText, p0)
    }
}
