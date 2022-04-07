package com.mohammadmawed.azkarapp.data

import androidx.lifecycle.LiveData

class ZikrRepo(private val zikrDao: ZikrDao) {

    fun getItemByID(id: Int): LiveData<List<Zikr>> {
        val readAllData: LiveData<List<Zikr>> = zikrDao.getAlsabahZikr(id)
        return readAllData
    }

    //To add Zikr is from here
    suspend fun addZikr(zikr: Zikr) {
        zikrDao.addZikr(Zikr(1, "Text oh oh oh oh ", 2,false, false))
        zikrDao.addZikr(Zikr(2, "sdigjh jghsj kjkshgjkshjklg jk ", 3,false, false))
        zikrDao.addZikr(Zikr(3, "353tar   jk ", 3,false, false))
    }
}