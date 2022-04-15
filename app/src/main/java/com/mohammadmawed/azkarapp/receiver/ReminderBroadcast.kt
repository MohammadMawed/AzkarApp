package com.mohammadmawed.azkarapp.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

import com.mohammadmawed.azkarapp.util.sendNotification


class ReminderBroadcast : BroadcastReceiver() {

    override fun onReceive(p0: Context, p1: Intent?) {

        // TODO: Step 1.9 add call to sendNotification
        val notificationManager = ContextCompat.getSystemService(
            p0,
            NotificationManager::class.java
        ) as NotificationManager


            notificationManager.sendNotification(
                "Azkar Alsabah Time. It's Azkar alsabah time. Don't forget to do them!",
                p0
            )
    }

}