package com.mohammadmawed.azkarapp.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.mohammadmawed.azkarapp.R
import com.mohammadmawed.azkarapp.util.sendNotification

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Optional remove toast
        Toast.makeText(context, "Hi", Toast.LENGTH_SHORT).show()

        // add call to sendNotification
        val notificationManager =
            ContextCompat.getSystemService(context, NotificationManager::class.java) as NotificationManager

        notificationManager.sendNotification(context.getText(R.string.notification_content).toString(), context)
    }

}