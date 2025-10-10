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
    OCEAN_BLUE,
    SUNSET_ORANGE,
    GOLDEN_LUXURY
}

fun ThemeMode.toAppTheme(): AppTheme {
    return when (this) {
        ThemeMode.LIGHT -> AppTheme.LIGHT
        ThemeMode.DARK -> AppTheme.DARK
        ThemeMode.OCEAN_BLUE -> AppTheme.OCEAN_BLUE
        ThemeMode.SUNSET_ORANGE -> AppTheme.SUNSET_ORANGE
        ThemeMode.GOLDEN_LUXURY -> AppTheme.GOLDEN_LUXURY
    }
}

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

private val GoldenLuxuryColorScheme = lightColorScheme(
    primary = LuxuryGold,
    secondary = RichGold,
    tertiary = BronzeAccent,
    background = IvoryWhite,
    surface = ChampagneGold,
    onPrimary = DeepBlack,
    onSecondary = DeepBlack,
    onTertiary = Color.White,
    onBackground = DeepBlack,
    onSurface = DeepBlack,
    primaryContainer = GoldShimmer,
    onPrimaryContainer = DeepBlack,
    secondaryContainer = ChampagneGold,
    onSecondaryContainer = DeepBlack
)

@Composable
fun DemoConductorTheme(
    theme: AppTheme = AppTheme.LIGHT,
    content: @Composable () -> Unit
) {
    val colorScheme = when (theme) {
        AppTheme.LIGHT -> LightColorScheme
        AppTheme.DARK -> DarkColorScheme
        AppTheme.OCEAN_BLUE -> OceanBlueColorScheme
        AppTheme.SUNSET_ORANGE -> SunsetOrangeColorScheme
        AppTheme.GOLDEN_LUXURY -> GoldenLuxuryColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
