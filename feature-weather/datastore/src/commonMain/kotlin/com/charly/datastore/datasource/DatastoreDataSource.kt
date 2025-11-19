package com.charly.datastore.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DatastoreDataSource(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun saveLongValue(key: String, value: Long) {
        dataStore.edit { preferences ->
            preferences[longPreferencesKey(key)] = value
        }
    }

    fun getLongValue(key: String): Flow<Long?> {
        return dataStore.data.map { preferences ->
            preferences[longPreferencesKey(key)]
        }
    }
}
