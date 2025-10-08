package com.example.democonductor.presentation.home

import com.example.democonductor.domain.model.ThemeMode

sealed interface HomeIntent {
    object ToggleThemeSelector : HomeIntent
    object DismissThemeSelector : HomeIntent
    data class SelectTheme(val themeMode: ThemeMode) : HomeIntent
}