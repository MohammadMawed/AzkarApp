package com.mohammadmawed.azkarapp.ui

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.mohammadmawed.azkarapp.data.*
import com.mohammadmawed.azkarapp.receiver.ReminderBroadcast
import com.mohammadmawed.azkarapp.util.cancelNotifications
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnspecifiedImmutableFlag")

class ZikrViewModel @ViewModelInject constructor(
    application: Application,
    private val zikrDao: ZikrDao,
    private val repo: ZikrRepo,
    private val preferencesManager: PreferencesManager
) : AndroidViewModel(application) {

    private val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val notifyIntent = Intent(application, ReminderBroadcast::class.java)

    private var _sendNotificationMutableLiveData: MutableLiveData<Boolean> =
        MutableLiveData()

    private var _snackBarSendNotificationMutableLiveData: MutableLiveData<Boolean> =
        MutableLiveData()

    private var _islamicCalendarMutableLiveData: MutableLiveData<String> =
        MutableLiveData()

    private val _notificationsOnMutableLiveDate: MutableLiveData<Boolean> =
        MutableLiveData()

    val islamicCalendarLiveData: LiveData<String> =
        _islamicCalendarMutableLiveData

    val snackBarSendNotificationLiveData: LiveData<Boolean> =
        _snackBarSendNotificationMutableLiveData

    val notificationsOnLiveDate: LiveData<Boolean> =
        _notificationsOnMutableLiveDate

    //val dao = zikrDao.getAlsabahZikr().asLiveData()

    init {

        repo.notifyPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                getApplication(),
                0,
                notifyIntent,
                PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                getApplication(),
                0,
                notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        //Calling all functions when the viewModel is initialized
        islamicDate()
        reminderNotification()

    }

    fun itemById(id: Int): Flow<List<Zikr>> {
       return repo.getItemByID(id)
    }

    private fun islamicDate() {
        val hijriDate = repo.islamicDate()
        _islamicCalendarMutableLiveData.postValue(hijriDate)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")

    fun reminderNotification() {

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val formatted = current.format(formatter)

        Log.d("date", formatted)

        val notificationManager =
            ContextCompat.getSystemService(getApplication(), NotificationManager::class.java)
                    as NotificationManager

        notificationManager.cancelNotifications()


        // Set the alarm to start at 8:30 a.m.
        Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 2)
            set(Calendar.MINUTE, 25)

            //Prevent sending more than one notification to the user

            if (this.time < Date()) this.add(Calendar.DAY_OF_MONTH, 1)

            //Wake up the device to fire the alarm at approximately 3:00 p.m., and repeat once a day at the same time
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                this.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                notifyPendingIntent
            )
        }

    }

}