package com.mohammadmawed.azkarapp.ui

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.AlarmManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mohammadmawed.azkarapp.data.Zikr
import com.mohammadmawed.azkarapp.data.ZikrDatabase
import com.mohammadmawed.azkarapp.data.ZikrRepo
import com.mohammadmawed.azkarapp.receiver.ReminderBroadcast
import com.mohammadmawed.azkarapp.util.cancelNotifications
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.M)
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


        notifyPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(getApplication(), 0, notifyIntent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getBroadcast(getApplication(), 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

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

        val triggerTime = SystemClock.elapsedRealtime()

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val formatted = current.format(formatter)

        Log.d("date", formatted)
        if (formatted == "02:19" || formatted == "02:20") {
            // TODO: Step 1.15 call cancel notification
            val notificationManager =
                ContextCompat.getSystemService(
                    context,
                    NotificationManager::class.java
                ) as NotificationManager

            notificationManager.cancelNotifications()

            AlarmManagerCompat.setExactAndAllowWhileIdle(
                alarmManager,
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime,
                notifyPendingIntent
            )
        }
    }
}