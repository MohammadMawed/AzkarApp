package com.mohammadmawed.azkarapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Singleton

const val PR_NAME = "setting"

@Singleton
class PreferencesManager(context: Context) {

    private val dataStore: DataStore<Preferences> = context.createDataStore("settings")

    suspend fun save(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit {
            it[dataStoreKey] = value
        }
    }

    suspend fun read(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        val pref = dataStore.data.first()
        return pref[dataStoreKey]
    }

}