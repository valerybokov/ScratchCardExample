package com.scratchcardexample.core.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.scratchcardexample.core.domain.repository.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidSettings @Inject constructor(
    @param:ApplicationContext private val appContext: Context
): Settings {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun raw(): Flow<Preferences> = appContext.dataStore.data

    override suspend fun write(key: String, value: String) {
        appContext.dataStore.edit { prefs ->
            val prefsKey = stringPreferencesKey(key)
            prefs[prefsKey] = value
        }
    }

    override fun readString(key: String, defaultValue: String): Flow<String> {
        return appContext.dataStore.data.map { prefs ->
            val prefsKey = stringPreferencesKey(key)
            prefs[prefsKey] ?: defaultValue
        }
    }

    override suspend fun readIntSuspend(key: String, defaultValue: Int): Int {
        val prefs = appContext.dataStore.data.first()
        val prefsKey = intPreferencesKey(key)
        return prefs[prefsKey] ?: defaultValue
    }

    override fun readInt(key: String, defaultValue: Int): Flow<Int> {
        return appContext.dataStore.data.map { prefs ->
            val prefsKey = intPreferencesKey(key)
            prefs[prefsKey] ?: defaultValue
        }
    }

    override suspend fun write(key: String, value: Int) {
        appContext.dataStore.edit { prefs ->
            val prefsKey = intPreferencesKey(key)
            prefs[prefsKey] = value
        }
    }
}