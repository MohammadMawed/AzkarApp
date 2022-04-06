package com.mohammadmawed.azkarapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ZikrDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addZikr(zikr: Zikr)

    @Query("SELECT * FROM zikr ORDER BY id ASC")
    fun getAlsabahZikr(): LiveData<List<Zikr>>

}