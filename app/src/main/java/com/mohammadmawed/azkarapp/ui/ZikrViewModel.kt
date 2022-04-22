package com.mohammadmawed.azkarapp.ui

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mohammadmawed.azkarapp.data.PreferencesManager
import com.mohammadmawed.azkarapp.data.Zikr
import com.mohammadmawed.azkarapp.data.ZikrDao
import com.mohammadmawed.azkarapp.data.ZikrRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnspecifiedImmutableFlag")

class ZikrViewModel @ViewModelInject constructor(
    application: Application,
    private val zikrDao: ZikrDao,
    private val repo: ZikrRepo,
    private val preferencesManager: PreferencesManager
) : AndroidViewModel(application) {

    private var _sendNotificationMutableLiveData: MutableLiveData<Boolean> =
        MutableLiveData()

    private var _snackBarSendNotificationMutableLiveData: MutableLiveData<Boolean> =
        MutableLiveData()

    private var _islamicCalendarMutableLiveData: MutableLiveData<String> =
        MutableLiveData()

    private val _notificationsOnSharedFlow: MutableSharedFlow<Boolean> =
        MutableSharedFlow()

    val islamicCalendarLiveData: LiveData<String> =
        _islamicCalendarMutableLiveData

    val snackBarSendNotificationLiveData: LiveData<Boolean> =
        _snackBarSendNotificationMutableLiveData

    var notificationsOnSharedFlow = _notificationsOnSharedFlow.asSharedFlow()

    val notificationRefFlow = preferencesManager.notificationRefFlow
    val notificationTimeHourFlow = preferencesManager.notificationTimeHourFlow
    val notificationTimeMinuteFlow = preferencesManager.notificationTimeMinuteFlow

    //val dao = zikrDao.getAlsabahZikr().asLiveData()


    init {
        //Calling all functions when the viewModel is initialized
        islamicDate()
        remindUserAtTime(application)
        //changeLanguage(application)

    }

    fun itemById(id: Int, alsabah: Boolean): Flow<List<Zikr>> {
        return repo.getItemByID(id, alsabah)
    }

    fun getAlmasahZikr(id: Int, alsabah: Boolean): Flow<List<Zikr>> {
        return repo.getAlmasahZikr(id, alsabah)
    }
    fun getZikr(): Flow<List<Zikr>> {
        return repo.getZikr()
    }

    private fun islamicDate() {
        val hijriDate = repo.islamicDate()
        _islamicCalendarMutableLiveData.postValue(hijriDate)
    }

    private fun remindUserAtTime(context: Context) {
        viewModelScope.launch {
            val hour = notificationTimeHourFlow.first()
            val minute = notificationTimeMinuteFlow.first()
            val per = notificationRefFlow.first()
            if (per) {
                repo.reminderNotification(context, hour, minute)
            } else {
                repo.cancelNotification(context)
            }
        }

    }

    fun saveNotificationSettings(value: Boolean, context: Context) {
        viewModelScope.launch {
            preferencesManager.saveNotification(value, context)
        }
    }

    fun saveNotificationSettingsHour(value: Int, context: Context) {
        viewModelScope.launch {
            preferencesManager.saveNotificationHour(value, context)
        }
    }

    fun saveNotificationSettingsMinute(value: Int, context: Context) {
        viewModelScope.launch {
            preferencesManager.saveNotificationMinute(value, context)
        }
    }
}