package com.mohammadmawed.azkarapp.data

import androidx.lifecycle.LiveData

class ZikrRepo(private val zikrDao: ZikrDao) {

    val readAllData: LiveData<List<Zikr>> = zikrDao.getAlsabahZikr()

    //To add Zikr is from here
    suspend fun addZikr(zikr: Zikr){
        zikrDao.addZikr(Zikr(0, "Text oh oh oh oh ", 2,false, false))
    }
}