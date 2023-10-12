package com.mohammadmawed.azkarapp.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.mohammadmawed.azkarapp.R

import com.mohammadmawed.azkarapp.util.sendNotification


class ReminderBroadcast : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(p0: Context, p1: Intent?) {
        val resources: Resources = p0.resources
        val notificationText = resources.getString(R.string.notification_text)


        //add call to sendNotification
        val notificationManager = ContextCompat.getSystemService(p0, NotificationManager::class.java)
                as NotificationManager

        notificationManager.sendNotification(notificationText, p0)

    }

}