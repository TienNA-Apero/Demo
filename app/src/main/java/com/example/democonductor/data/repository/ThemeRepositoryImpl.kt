package com.example.democonductor.data.repository

import com.example.democonductor.data.local.ThemeLocalDataSource
import com.example.democonductor.domain.model.ThemeMode
import com.example.democonductor.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ThemeRepositoryImpl @Inject constructor(
    private val themeLocalDataSource: ThemeLocalDataSource
) : ThemeRepository {

    override fun getThemeMode(): Flow<ThemeMode> {
        return themeLocalDataSource.getThemeMode().map { themeName ->
            try {
                ThemeMode.valueOf(themeName)
            } catch (e: IllegalArgumentException) {
                ThemeMode.LIGHT // Default fallback
            }
        }
    }

    override suspend fun updateThemeMode(themeMode: ThemeMode) {
        themeLocalDataSource.saveThemeMode(themeMode.name)
    }
}