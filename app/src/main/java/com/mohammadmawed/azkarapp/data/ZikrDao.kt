package com.mohammadmawed.azkarapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ZikrDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addZikr(zikr: Zikr)

    @Query("SELECT * FROM zikr WHERE id = :id AND alsabah = :alsabah")
    fun getAlsabahZikr(id: Int, alsabah: Boolean): Flow<List<Zikr>>

    @Query("SELECT * FROM zikr WHERE id = :id AND alsabah = :alsabah")
    fun getAlmasahZikr(id: Int, alsabah: Boolean): Flow<List<Zikr>>

    @Query("SELECT * FROM zikr")
    fun getZikr(): Flow<List<Zikr>>

}