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

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

private val OceanBlueColorScheme = lightColorScheme(
    primary = OceanBlue,
    secondary = SeaBlue,
    tertiary = Aqua,
    background = LightFoam,
    surface = LightFoam,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = OceanBlueDark,
    onSurface = OceanBlueDark,
    error = Coral,
    onError = Color.White,
    primaryContainer = SeaBlue,
    onPrimaryContainer = Color.White,
    secondaryContainer = Aqua,
    onSecondaryContainer = Color.White
)

@Composable
fun DemoConductorTheme(
    themeMode: ThemeMode = ThemeMode.LIGHT,
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeMode) {
        ThemeMode.OCEAN_BLUE -> OceanBlueColorScheme
        ThemeMode.DARK -> DarkColorScheme
        ThemeMode.LIGHT -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}