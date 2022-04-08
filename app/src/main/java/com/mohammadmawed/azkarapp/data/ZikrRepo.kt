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
        zikrDao.addZikr(Zikr(4, "أَصْـبَحْنا وَأَصْـبَحَ المُـلْكُ لله وَالحَمدُ لله ، لا إلهَ إلاّ اللّهُ وَحدَهُ لا شَريكَ لهُ، لهُ المُـلكُ ولهُ الحَمْـد، وهُوَ على كلّ شَيءٍ قدير ، رَبِّ أسْـأَلُـكَ خَـيرَ ما في هـذا اليوم وَخَـيرَ ما بَعْـدَه ، وَأَعـوذُ بِكَ مِنْ شَـرِّ ما في هـذا اليوم وَشَرِّ ما بَعْـدَه، رَبِّ أَعـوذُبِكَ مِنَ الْكَسَـلِ وَسـوءِ الْكِـبَر ، رَبِّ أَعـوذُ بِكَ مِنْ عَـذابٍ في النّـارِ وَعَـذابٍ في القَـبْر.", 1,false, false))
    }
}