package com.mohammadmawed.azkarapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mohammadmawed.azkarapp.util.NotificationUtils


class ReminderBroadcast : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {

        val _notificationUtils = p0?.let { NotificationUtils(it) }
        val _builder = _notificationUtils?.setNotification("Azkar Alsabah Time", "It's Azkar alsabah time. Don't forget to do them!")
        _notificationUtils?.getManager()!!.notify(101, _builder?.build())

    }

}