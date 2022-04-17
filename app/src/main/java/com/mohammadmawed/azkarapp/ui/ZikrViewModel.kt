package com.mohammadmawed.azkarapp.ui

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.mohammadmawed.azkarapp.data.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.IOException


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
        //Calling all functions when the viewModel is initialized
        islamicDate()
        remindUserAtTime(application)
    }

    fun itemById(id: Int): Flow<List<Zikr>> {
        return repo.getItemByID(id)
    }

    private fun islamicDate() {
        val hijriDate = repo.islamicDate()
        _islamicCalendarMutableLiveData.postValue(hijriDate)

    }

    private fun remindUserAtTime(application: Application) {
        repo.reminderNotification(application)
    }

    suspend fun readSettings(key: String): Boolean{

        val result: Boolean = try {
            preferencesManager.read(key)
            true
        }catch (exception: IOException){
            false
        }
        return result
    }

    fun saveSettings(key: String, value: String) = viewModelScope.launch{
        repo.saveSettings(key, value)
    }
    fun readLanguageSettings(key: String, context: Context) = viewModelScope.launch{
        repo.readLanguageSettings(key, context)
    }

}