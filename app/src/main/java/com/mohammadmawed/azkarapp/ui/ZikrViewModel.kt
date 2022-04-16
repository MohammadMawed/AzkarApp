package com.mohammadmawed.azkarapp.ui

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.CountDownTimer
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
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnspecifiedImmutableFlag")
class ZikrViewModel(application: Application) : AndroidViewModel(application) {

    private val REQUEST_CODE = 0
    private val TRIGGER_TIME = "TRIGGER_AT"

    private val minute: Long = 60_000L
    private val second: Long = 1_000L

    private val repo: ZikrRepo
    private lateinit var notifyPendingIntent: PendingIntent

    private val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private var prefs =
        application.getSharedPreferences("com.mohammadmawed.azkarapp", Context.MODE_PRIVATE)

    private val notifyIntent = Intent(application, ReminderBroadcast::class.java)

    private var _sendNotificationMutableLiveData: MutableLiveData<Boolean> =
        MutableLiveData()

    private var _snackBarSendNotificationMutableLiveData: MutableLiveData<Boolean> =
        MutableLiveData()

    private var _islamicCalendarMutableLiveData: MutableLiveData<String> =
        MutableLiveData()

    val islamicCalendarLiveData: LiveData<String> =
        _islamicCalendarMutableLiveData

    val snackBarSendNotificationLiveData: LiveData<Boolean> =
        _snackBarSendNotificationMutableLiveData

    private val _elapsedTime = MutableLiveData<Long>()
    val elapsedTime: LiveData<Long>
        get() = _elapsedTime

    var alarmOn: Boolean = false

    private lateinit var timer: CountDownTimer

    init {
        val zikrDao = ZikrDatabase.getDatabase(application).zikrDao()
        repo = ZikrRepo(zikrDao)

        //Calling all functions when the viewModel is initialized
        addZikr()
        islamicDate()

        createNotificationChannel(application)


    }

    private fun addZikr() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addZikr()
            repo.addZikr()
        }
    }
    private fun createNotificationChannel(context: Context){
        repo.createChannel(context)
    }

    fun itemById(id: Int): LiveData<List<Zikr>> {
        return repo.getItemByID(id)
    }

    private fun islamicDate(){
        val hijriDate = repo.islamicDate()
        _islamicCalendarMutableLiveData.postValue(hijriDate)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")

    fun reminderNotification() {

        notifyPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
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

        // Set the alarm to start at 8:30 a.m.
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 3)
            set(Calendar.MINUTE, 34)
        }
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val formatted = current.format(formatter)

        Log.d("date", formatted)

        //Wake up the device to fire the alarm at approximately 3:00 p.m., and repeat once a day at the same time
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                notifyPendingIntent
            )

        //}
    }

}