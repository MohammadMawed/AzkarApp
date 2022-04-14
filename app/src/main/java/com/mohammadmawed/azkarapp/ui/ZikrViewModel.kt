package com.mohammadmawed.azkarapp.ui

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.os.SystemClock
import androidx.core.app.AlarmManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mohammadmawed.azkarapp.R
import com.mohammadmawed.azkarapp.data.Zikr
import com.mohammadmawed.azkarapp.data.ZikrDatabase
import com.mohammadmawed.azkarapp.data.ZikrRepo
import com.mohammadmawed.azkarapp.receiver.AlarmReceiver
import com.mohammadmawed.azkarapp.util.cancelNotifications
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("UnspecifiedImmutableFlag")
class ZikrViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: ZikrRepo

    init {
        val zikrDao = ZikrDatabase.getDatabase(application).zikrDao()
        repo = ZikrRepo(zikrDao)

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

}