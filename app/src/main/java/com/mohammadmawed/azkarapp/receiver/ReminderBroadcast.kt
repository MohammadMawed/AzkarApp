package com.mohammadmawed.azkarapp.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

import com.mohammadmawed.azkarapp.util.sendNotification


class ReminderBroadcast : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(p0: Context, p1: Intent?) {
        val notificationManager = ContextCompat.getSystemService(
            p0,
            NotificationManager::class.java
        ) as NotificationManager

        // Retrieve the notification type from the intent
        val notificationType = p1?.getStringExtra("notification_type") ?: return
        Log.d("ReminderBroadcast", "Received $notificationType notification.")

        // Determine the message based on the notification type
        val message = when (notificationType) {
            "morning" -> "صباح النور! اللهم بك نستعين على ذكرك وشكرك وحسن عبادتك. وقت أذكار الصباح."
            "evening" -> "مساء السكينة! اللهم إني أمسيت أشهدك وأشهد حملة عرشك بأنك أنت الله لا إله إلا أنت. حان وقت أذكار المساء."

            else -> return // Unknown notification type, do nothing
        }

        // Call to sendNotification, adapted to use the message determined above
        notificationManager.sendNotification(message, p0)
    }
}