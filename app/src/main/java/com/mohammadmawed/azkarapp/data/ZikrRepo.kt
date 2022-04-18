package com.mohammadmawed.azkarapp.data

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.mohammadmawed.azkarapp.receiver.ReminderBroadcast
import com.mohammadmawed.azkarapp.util.cancelNotifications
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject


class ZikrRepo @Inject constructor(
    private val zikrDao: ZikrDao,
    val preferencesManager: PreferencesManager
) {

    fun getItemByID(id: Int): Flow<List<Zikr>> {
        val readAllData: Flow<List<Zikr>> = zikrDao.getAlsabahZikr(id)
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
    @SuppressLint("SimpleDateFormat", "UnspecifiedImmutableFlag")

    fun reminderNotification(context: Context) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notifyPendingIntent: PendingIntent
        val notifyIntent = Intent(context, ReminderBroadcast::class.java)

        notifyPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                context,
                0,
                notifyIntent,
                PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                context,
                0,
                notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val notificationManager =
            ContextCompat.getSystemService(context, NotificationManager::class.java)
                    as NotificationManager

        notificationManager.cancelNotifications()

        // Set the alarm to start at 8:30 a.m.
        Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 4)
            set(Calendar.MINUTE, 16)

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

    @SuppressLint("UnspecifiedImmutableFlag")
    fun cancelNotification(context: Context) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notifyPendingIntent: PendingIntent
        val notifyIntent = Intent(context, ReminderBroadcast::class.java)

        notifyPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                context,
                0,
                notifyIntent,
                PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                context,
                0,
                notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val notificationManager =
            ContextCompat.getSystemService(context, NotificationManager::class.java)
                    as NotificationManager

        notificationManager.cancelNotifications()

        alarmManager.cancel(notifyPendingIntent)
    }

    fun changeLanguage(lang: String, context: Context) {
        if (lang == "ar") {
            val locale = Locale("ar")
            Locale.setDefault(locale)
            val resources: Resources? = context.resources
            val configuration: Configuration? = resources?.configuration
            configuration?.locale = locale
            configuration?.setLayoutDirection(locale)
            resources?.updateConfiguration(configuration, resources.displayMetrics)
        } else if (lang == "en") {
            val locale = Locale("en")
            Locale.setDefault(locale)
            val resources: Resources? = context.resources
            val configuration: Configuration? = resources?.configuration
            configuration?.locale = locale
            configuration?.setLayoutDirection(locale)
            resources?.updateConfiguration(configuration, resources.displayMetrics)
        }
    }



}