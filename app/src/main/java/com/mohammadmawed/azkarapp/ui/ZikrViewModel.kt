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
        remindUserAtTime(application)
        remindUserAtTime1(application)
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

    private fun remindUserAtTime(context: Context) {
        viewModelScope.launch {
            val hour = notificationTimeHourFlow.first()
            val minute = notificationTimeMinuteFlow.first()
            val per = notificationRefFlow.first()
            if (per) {
                repo.reminderNotification(context, hour, minute, "first") // For the first notification
            } else {
                repo.cancelNotification(context)
            }
        }
    }

    private fun remindUserAtTime1(context: Context) {
        viewModelScope.launch {
            val hour = notificationSecTimeHourFlow.first()
            val minute = notificationSecTimeMinuteFlow.first()
            val per = notificationSecRefFlow.first()
            if (per) {
                repo.reminderNotification(context, hour, minute, "second") // For the second notification
            } else {
                repo.cancelNotification(context)
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