package com.mohammadmawed.azkarapp.ui

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.mohammadmawed.azkarapp.data.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.math.min


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

    val languagePrefFlow = preferencesManager.languageRefFlow
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

    fun itemById(id: Int): Flow<List<Zikr>> {
        return repo.getItemByID(id)
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

    private fun changeLanguage(context: Context) {
        viewModelScope.launch {
            val language = languagePrefFlow.first()
            Log.d("lang", language)
            if (language == "ar") {
                repo.changeLanguage("ar", context)
            } else if (language == "en") {
                repo.changeLanguage("en", context)
            }
        }
    }

    fun saveLanguageSettings(value: String, context: Context) {
        viewModelScope.launch {
            preferencesManager.saveLanguageRef(value, context)
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