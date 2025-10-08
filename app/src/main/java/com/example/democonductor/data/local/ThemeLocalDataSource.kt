package com.example.democonductor.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ThemeLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
        const val DEFAULT_THEME = "LIGHT"
    }

    fun getThemeMode(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[THEME_MODE_KEY] ?: DEFAULT_THEME
        }
    }

    suspend fun saveThemeMode(themeMode: String) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = themeMode
        }
    }
}