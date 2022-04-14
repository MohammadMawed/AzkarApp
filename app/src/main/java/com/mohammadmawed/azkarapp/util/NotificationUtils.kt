/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mohammadmawed.azkarapp.util

import android.app.*
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.mohammadmawed.azkarapp.MainActivity
import com.mohammadmawed.azkarapp.R
import com.mohammadmawed.azkarapp.receiver.ReminderBroadcast


// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
class NotificationUtils(base: Context) : ContextWrapper(base) {
    private var _notificationManager: NotificationManager? = null
    private val _context: Context = base


    private val contentIntent = Intent(applicationContext, MainActivity::class.java)
    private val contentPendingIntent: PendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )


    init {
        createChannel()
    }

    fun setNotification(title: String?, body: String?): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.drawable.tasbih)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)

            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "CHANNEL_ID",
                "TIMELINE_CHANNEL_NAME",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            getManager()!!.createNotificationChannel(channel)
        }
    }

    fun getManager(): NotificationManager? {
        if (_notificationManager == null) {
            _notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        }
        return _notificationManager
    }

    fun setReminder(timeInMillis: Long) {
        val _intent = Intent(_context, ReminderBroadcast::class.java)
        val _pendingIntent = PendingIntent.getBroadcast(_context, 0, _intent, 0)
        val _alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        _alarmManager[AlarmManager.RTC_WAKEUP, timeInMillis] = _pendingIntent
    }

}
