package com.mohammadmawed.azkarapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.mohammadmawed.azkarapp.data.Zikr
import com.mohammadmawed.azkarapp.data.ZikrDatabase
import com.mohammadmawed.azkarapp.data.ZikrRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ZikrViewModel(application: Application): AndroidViewModel(application) {

    private val readAllData: LiveData<List<Zikr>>
    private val repo: ZikrRepo

    init {
        val zikrDao = ZikrDatabase.getDatabase(application).zikrDao()
        repo = ZikrRepo(zikrDao)
        readAllData = repo.readAllData
    }

    fun addZikr(zikr: Zikr){
        viewModelScope.launch(Dispatchers.IO){
            repo.addZikr(zikr)
        }
    }

}