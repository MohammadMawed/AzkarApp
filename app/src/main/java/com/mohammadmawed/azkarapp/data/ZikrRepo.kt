package com.mohammadmawed.azkarapp.data

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.mohammadmawed.azkarapp.receiver.ReminderBroadcast
import com.mohammadmawed.azkarapp.util.cancelNotifications
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


class ZikrRepo @Inject constructor(private val zikrDao: ZikrDao) {
    companion object {
        const val MORNING_NOTIFICATION_REQUEST_CODE = 100
        const val EVENING_NOTIFICATION_REQUEST_CODE = 101
        const val DEG_TO_RAD = Math.PI / 180
        const val RAD_TO_DEG = 180 / Math.PI
    }

    fun getItemByID(id: Int, alsabah: Boolean): Flow<List<Zikr>> {
        val readAllData: Flow<List<Zikr>> = zikrDao.getAlsabahZikr(id, alsabah)
        return readAllData
    }

    fun getAlmasahZikr(id: Int, alsabah: Boolean): Flow<List<Zikr>> {
        val readAllData: Flow<List<Zikr>> = zikrDao.getAlmasahZikr(id, alsabah)
        return readAllData
    }

    fun getZikrExtra(wasRead: Boolean): Flow<List<Zikr>> {
        val readAllData: Flow<List<Zikr>> = zikrDao.getZikrExtra(wasRead)
        return readAllData
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun islamicDate(): String {

        val calendar = Calendar.getInstance()
        val year = calendar[java.util.Calendar.YEAR]
        val month = calendar[java.util.Calendar.MONTH] + 1

        //Change the number behind the brackets
        val day = calendar[java.util.Calendar.DAY_OF_MONTH]

        val dt: LocalDate = LocalDate.of(year, month, day)

        //When change the language locally, display the date in the tight language

        //val locale: String = Locale.getDefault().toLanguageTag()
        //val fr = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale(locale))

        // convert to Hijri
        val hijrahDate = HijrahDate.from(dt)
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        return formatter.format(hijrahDate)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnspecifiedImmutableFlag", "ScheduleExactAlarm")
    fun reminderNotification(context: Context, hour: Int, minute: Int, notificationType: String, requestCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderBroadcast::class.java).apply {
            putExtra("notification_type", notificationType)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val targetCal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)

            // If the set time has already passed for today, schedule for tomorrow
            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                targetCal.timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                targetCal.timeInMillis,
                pendingIntent
            )
        }

        Log.d("ZikrRepo", "Scheduled $notificationType notification for ${targetCal.time}")
    }



    @SuppressLint("UnspecifiedImmutableFlag")
    fun cancelNotification(context: Context, requestCode: Int) {
        val intent = Intent(context, ReminderBroadcast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }


}