package com.example.democonductor.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.democonductor.domain.model.ThemeMode

enum class AppTheme {
    LIGHT,
    DARK,
    SUNSET_ORANGE
}

fun ThemeMode.toAppTheme(): AppTheme {
    return when (this) {
        ThemeMode.LIGHT -> AppTheme.LIGHT
        ThemeMode.DARK -> AppTheme.DARK
        ThemeMode.SUNSET_ORANGE -> AppTheme.SUNSET_ORANGE
    }
}

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    secondary = Color(0xFFCCC2DC),
    tertiary = Color(0xFFEFB8C8)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6650a4),
    secondary = Color(0xFF625b71),
    tertiary = Color(0xFF7D5260)
)

private val SunsetOrangeColorScheme = lightColorScheme(
    primary = SunsetOrange,
    secondary = CoralPink,
    tertiary = AmberGlow,
    background = SunsetCream,
    surface = SunsetCream,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White
)

@Composable
fun DemoConductorTheme(
    theme: AppTheme = AppTheme.LIGHT,
    content: @Composable () -> Unit
) {
    val colorScheme = when (theme) {
        AppTheme.LIGHT -> LightColorScheme
        AppTheme.DARK -> DarkColorScheme
        AppTheme.SUNSET_ORANGE -> SunsetOrangeColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}