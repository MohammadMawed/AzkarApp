package com.mohammadmawed.azkarapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

const val PR_NAME = "settings"

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(PR_NAME)

    companion object {
        val NOTIFICATION_TOGGLE: Preferences.Key<Boolean> = booleanPreferencesKey("notification")
        val NOTIFICATION_TIME_HOUR: Preferences.Key<Int> = intPreferencesKey("notification_time_hour")
        val NOTIFICATION_TIME_MINUTE: Preferences.Key<Int> = intPreferencesKey("notification_time_minute")
    }

    suspend fun saveNotification(value: Boolean, context: Context) {
        context.dataStore.edit {
            it[NOTIFICATION_TOGGLE] = value
        }
    }

    suspend fun saveNotificationHour(value: Int, context: Context) {
        context.dataStore.edit {
            it[NOTIFICATION_TIME_HOUR] = value
        }
    }

    suspend fun saveNotificationMinute(value: Int, context: Context) {
        context.dataStore.edit {
            it[NOTIFICATION_TIME_MINUTE] = value
        }
    }

    val notificationRefFlow: Flow<Boolean> = context.dataStore.data.map {
        it[NOTIFICATION_TOGGLE] ?: true
    }

    val notificationTimeHourFlow: Flow<Int> = context.dataStore.data.map {
        it[NOTIFICATION_TIME_HOUR] ?: 8
    }

    val notificationTimeMinuteFlow: Flow<Int> = context.dataStore.data.map {
        it[NOTIFICATION_TIME_MINUTE] ?: 0
    }
}