package com.example.democonductor.presentation.home

import com.example.democonductor.domain.model.ThemeMode

data class HomeState(
    val currentTheme: ThemeMode = ThemeMode.LIGHT,
    val showThemeSelector: Boolean = false,
    val availableThemes: List<ThemeMode> = ThemeMode.entries
)