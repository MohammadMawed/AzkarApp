package com.mohammadmawed.azkarapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application: Application(){
    override fun onCreate() {

        super.onCreate()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Create the NotificationChannel
                val name = getText(R.string.notification_channel_name)
                val importance = NotificationManager.IMPORTANCE_HIGH
                val mChannel =
                    NotificationChannel(getString(R.string.notification_channel_id), name, importance)
                mChannel.description = resources.getString(R.string.notification_content)
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(mChannel)
            }
    }
}