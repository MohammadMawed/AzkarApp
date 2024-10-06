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
import com.mohammadmawed.azkarapp.data.ZikrRepo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.TimeZone


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnspecifiedImmutableFlag")

class ZikrViewModel @ViewModelInject constructor(
    application: Application,
    private val repo: ZikrRepo,
    private val preferencesManager: PreferencesManager
) : AndroidViewModel(application) {


    private var _islamicCalendarMutableLiveData: MutableLiveData<String> =
        MutableLiveData()

    private var _notificationsOnSharedFlow: MutableSharedFlow<Boolean> =
        MutableSharedFlow()

    val visiblePermissionDialog = mutableListOf<String>()

    val islamicCalendarLiveData: LiveData<String> =
        _islamicCalendarMutableLiveData

    var notificationsOnSharedFlow = _notificationsOnSharedFlow.asSharedFlow()

    val notificationRefFlow = preferencesManager.notificationRefFlow
    val notificationTimeHourFlow = preferencesManager.notificationTimeHourFlow
    val notificationTimeMinuteFlow = preferencesManager.notificationTimeMinuteFlow

    val notificationSecRefFlow = preferencesManager.notificationSecRefFlow
    val notificationSecTimeHourFlow = preferencesManager.notificationSecTimeHourFlow
    val notificationSecTimeMinuteFlow = preferencesManager.notificationSecTimeMinuteFlow

    //val dao = zikrDao.getAlsabahZikr().asLiveData()


    init {
        //Calling all functions when the viewModel is initialized
        islamicDate()
        scheduleNotifications(application)
    }

    fun itemById(id: Int, alsabah: Boolean): Flow<List<Zikr>> {
        return repo.getItemByID(id, alsabah)
    }

    fun getAlmasahZikr(id: Int, alsabah: Boolean): Flow<List<Zikr>> {
        return repo.getAlmasahZikr(id, alsabah)
    }

    fun getZikrExtra(wasRead: Boolean): Flow<List<Zikr>> {
        return repo.getZikrExtra(wasRead)
    }

    private fun islamicDate() {
        val hijriDate = repo.islamicDate()
        _islamicCalendarMutableLiveData.postValue(hijriDate)
    }


    private fun scheduleNotifications(context: Context) {
        viewModelScope.launch {
            val morningHour = notificationTimeHourFlow.first()
            val morningMinute = notificationTimeMinuteFlow.first()
            if (notificationRefFlow.first()) {
                repo.reminderNotification(context, morningHour, morningMinute, "morning", ZikrRepo.MORNING_NOTIFICATION_REQUEST_CODE)
            } else {
                repo.cancelNotification(context, ZikrRepo.MORNING_NOTIFICATION_REQUEST_CODE)
            }

            val eveningHour = notificationSecTimeHourFlow.first()
            val eveningMinute = notificationSecTimeMinuteFlow.first()
            if (notificationSecRefFlow.first()) {
                repo.reminderNotification(context, eveningHour, eveningMinute, "evening", ZikrRepo.EVENING_NOTIFICATION_REQUEST_CODE)
            } else {
                repo.cancelNotification(context, ZikrRepo.EVENING_NOTIFICATION_REQUEST_CODE)
            }
        }
    }
    fun saveNotificationSettings(value: Boolean, context: Context) {
        viewModelScope.launch {
            if (value){
                _notificationsOnSharedFlow.emit(true)
            }
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

    fun saveNotificationSecSettings(value: Boolean, context: Context) {
        viewModelScope.launch {
            if (value){
                _notificationsOnSharedFlow.emit(true)
            }
            preferencesManager.saveNotificationSec(value, context)
        }
    }

    fun saveNotificationSecSettingsHour(value: Int, context: Context) {
        viewModelScope.launch {
            preferencesManager.saveNotificationSecHour(value, context)
        }
    }

    fun saveNotificationSecSettingsMinute(value: Int, context: Context) {
        viewModelScope.launch {
            preferencesManager.saveNotificationSecMinute(value, context)
        }
    }
    fun dismissDialog(){
        visiblePermissionDialog.removeLast()
    }
    fun onPermissionResult(permission: String, isGranted: Boolean){
        if (!isGranted){
            visiblePermissionDialog.add(0, permission)
        }
    }
}