package com.mohammadmawed.azkarapp.di

import android.app.Application
import androidx.room.Room
import com.mohammadmawed.azkarapp.data.ZikrDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)

object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
        callback: ZikrDatabase.CallBack
    ) = Room.databaseBuilder(app,
        ZikrDatabase::class.java, "zikr")
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()

    @Provides
    fun provideZikrDao(db: ZikrDatabase) = db.zikrDao()

    //Creating coroutine for when getting data from the db using suspend fun
    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope