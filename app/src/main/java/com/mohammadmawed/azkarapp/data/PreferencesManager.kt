package com.mohammadmawed.azkarapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
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

    companion object{
        val LANGUAGE_PREF: Preferences.Key<String> = stringPreferencesKey("lang")
        val NOTIFICATION_TOGGLE: Preferences.Key<Boolean> = booleanPreferencesKey("notification")
    }

    suspend fun saveLanguageRef(value: String, context: Context) {
        context.dataStore.edit {
            it[LANGUAGE_PREF] = value
        }
    }
    suspend fun saveNotification(value: Boolean, context: Context) {
        context.dataStore.edit {
            it[NOTIFICATION_TOGGLE] = value
        }
    }

    val languageRefFlow: Flow<String> = context.dataStore.data.map {
        it[LANGUAGE_PREF] ?: "en"
    }
    val notificationRefFlow: Flow<Boolean> = context.dataStore.data.map {
        it[NOTIFICATION_TOGGLE]!!
    }
}