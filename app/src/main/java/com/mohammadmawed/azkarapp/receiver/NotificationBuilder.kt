package com.mohammadmawed.azkarapp.receiver

import android.app.Notification
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.mohammadmawed.azkarapp.R

class NotificationBuilder(context: Context) {

    @RequiresApi(Build.VERSION_CODES.O)
    val repliedNotification = Notification.Builder(context, "CHANNEL_ID")
        .setSmallIcon(R.drawable.tasbih)
        .setContentTitle("Time For Zikr!")
        .setContentText("This is the time of zikr. Make now!")
        .build()

}