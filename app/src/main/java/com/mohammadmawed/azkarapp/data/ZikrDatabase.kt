package com.mohammadmawed.azkarapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Zikr::class], version = 1, exportSchema = false)

abstract class ZikrDatabase: RoomDatabase() {

    abstract fun zikrDao(): ZikrDao
    companion object{
        @Volatile
        //Creating one instance of our database to prevent duplicating
        private var INSTANCE: ZikrDatabase? = null

        fun getDatabase(context: Context): ZikrDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ZikrDatabase::class.java,
                    "zikr"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}