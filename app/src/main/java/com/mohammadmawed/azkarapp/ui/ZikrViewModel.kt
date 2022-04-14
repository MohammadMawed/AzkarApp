package com.mohammadmawed.azkarapp.ui

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.AlarmManagerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mohammadmawed.azkarapp.data.Zikr
import com.mohammadmawed.azkarapp.data.ZikrDatabase
import com.mohammadmawed.azkarapp.data.ZikrRepo
import com.mohammadmawed.azkarapp.receiver.ReminderBroadcast
import com.mohammadmawed.azkarapp.util.NotificationUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@SuppressLint("UnspecifiedImmutableFlag")
class ZikrViewModel(application: Application) : AndroidViewModel(application) {

    private val REQUEST_CODE = 0
    private val TRIGGER_TIME = "TRIGGER_AT"

    private val repo: ZikrRepo
    private val notifyPendingIntent: PendingIntent

    private val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val notifyIntent = Intent(application, ReminderBroadcast::class.java)

    private var _sendNotificationMutableLiveData: MutableLiveData<Boolean> =
        MutableLiveData()

    val sendNotificationLiveData: LiveData<Boolean> =
        _sendNotificationMutableLiveData

    init {
        val zikrDao = ZikrDatabase.getDatabase(application).zikrDao()
        repo = ZikrRepo(zikrDao)

        notifyPendingIntent = PendingIntent.getBroadcast(
            getApplication(),
            REQUEST_CODE,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

    }

    fun addZikr() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addZikr(Zikr(0, "Text oh oh oh oh ", "", 2, false, false))
            repo.addZikr(Zikr(1, "sdigjh jghsj kjkshgjkshjklg jk ", "", 3, false, false))
        }
    }

    fun itemById(id: Int): LiveData<List<Zikr>> {
        return repo.getItemByID(id)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    fun reminderNotification(context: Context) {
        val _notificationUtils = NotificationUtils(context)
        val _currentTime = System.currentTimeMillis()
        val tenSeconds = (1000).toLong()
        val _triggerReminder = _currentTime + tenSeconds //triggers a reminder after 10 seconds.

        val sdf = SimpleDateFormat("hh:mm")
        val currentDate = sdf.format(Date())

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val formatted = current.format(formatter)

        Log.d("date", formatted)
        if (formatted == "18:46" || formatted == "18:47") {
            alarmManager.set(AlarmManager.RTC_WAKEUP, 1000, notifyPendingIntent)
            _notificationUtils.setReminder(_triggerReminder)
        }
    }
}