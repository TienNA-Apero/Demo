package com.example.democonductor.domain.repository

import com.example.democonductor.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    fun getThemeMode(): Flow<ThemeMode>
    suspend fun updateThemeMode(themeMode: ThemeMode)
}