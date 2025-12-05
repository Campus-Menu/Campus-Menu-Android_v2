package com.arikan.campusmenu.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private fun getThemeDarkColors(themeName: String) = when (themeName) {
    "Mavi" -> darkColorScheme(
        primary = Blue300,
        secondary = Blue500,
        tertiary = MintGreen,
        background = Color(0xFF121212),
        surface = Color(0xFF1E1E1E),
        onPrimary = Color.Black,
        onSecondary = Color.White,
        onTertiary = Color.Black,
        onBackground = Color(0xFFE0E0E0),
        onSurface = Color(0xFFE0E0E0),
        primaryContainer = Blue600,
        onPrimaryContainer = Blue300,
        secondaryContainer = Color(0xFF1A3A52),
        onSecondaryContainer = Blue300
    )
    "Yeşil" -> darkColorScheme(
        primary = Green300,
        secondary = Green500,
        tertiary = MintGreen,
        background = Color(0xFF121212),
        surface = Color(0xFF1E1E1E),
        onPrimary = Color.Black,
        onSecondary = Color.White,
        onTertiary = Color.Black,
        onBackground = Color(0xFFE0E0E0),
        onSurface = Color(0xFFE0E0E0),
        primaryContainer = Green600,
        onPrimaryContainer = Green300,
        secondaryContainer = Color(0xFF1B3A2E),
        onSecondaryContainer = Green300
    )
    "Mor" -> darkColorScheme(
        primary = Purple300,
        secondary = Purple500,
        tertiary = MintGreen,
        background = Color(0xFF121212),
        surface = Color(0xFF1E1E1E),
        onPrimary = Color.Black,
        onSecondary = Color.White,
        onTertiary = Color.Black,
        onBackground = Color(0xFFE0E0E0),
        onSurface = Color(0xFFE0E0E0),
        primaryContainer = Purple600,
        onPrimaryContainer = Purple300,
        secondaryContainer = Color(0xFF3A1B52),
        onSecondaryContainer = Purple300
    )
    else -> darkColorScheme( // Turuncu (Varsayılan)
        primary = Orange300,
        secondary = Coral,
        tertiary = MintGreen,
        background = Color(0xFF121212),
        surface = Color(0xFF1E1E1E),
        onPrimary = Color.Black,
        onSecondary = Color.Black,
        onTertiary = Color.Black,
        onBackground = Color(0xFFE0E0E0),
        onSurface = Color(0xFFE0E0E0),
        primaryContainer = Orange600,
        onPrimaryContainer = Orange300,
        secondaryContainer = Color(0xFF5E3A2E),
        onSecondaryContainer = Coral,
        tertiaryContainer = DarkGreen,
        onTertiaryContainer = MintGreen,
        surfaceVariant = Color(0xFF2A2A2A),
        onSurfaceVariant = Color(0xFFB0B0B0)
    )
}

private fun getThemeLightColors(themeName: String) = when (themeName) {
    "Mavi" -> lightColorScheme(
        primary = Blue500,
        secondary = Blue600,
        tertiary = MintGreen,
        background = BackgroundLight,
        surface = CardBackground,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onTertiary = TextDark,
        onBackground = TextDark,
        onSurface = TextDark,
        primaryContainer = BlueLight,
        onPrimaryContainer = Blue600,
        secondaryContainer = Blue300,
        onSecondaryContainer = Blue600
    )
    "Yeşil" -> lightColorScheme(
        primary = Green500,
        secondary = Green600,
        tertiary = MintGreen,
        background = BackgroundLight,
        surface = CardBackground,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onTertiary = TextDark,
        onBackground = TextDark,
        onSurface = TextDark,
        primaryContainer = GreenLight,
        onPrimaryContainer = Green600,
        secondaryContainer = Green300,
        onSecondaryContainer = Green600
    )
    "Mor" -> lightColorScheme(
        primary = Purple500,
        secondary = Purple600,
        tertiary = MintGreen,
        background = BackgroundLight,
        surface = CardBackground,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onTertiary = TextDark,
        onBackground = TextDark,
        onSurface = TextDark,
        primaryContainer = PurpleLight,
        onPrimaryContainer = Purple600,
        secondaryContainer = Purple300,
        onSecondaryContainer = Purple600
    )
    else -> lightColorScheme( // Turuncu (Varsayılan)
        primary = Orange500,
        secondary = Coral,
        tertiary = MintGreen,
        background = BackgroundLight,
        surface = CardBackground,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onTertiary = TextDark,
        onBackground = TextDark,
        onSurface = TextDark,
        primaryContainer = OrangeLight,
        onPrimaryContainer = Orange600,
        secondaryContainer = Peach,
        onSecondaryContainer = Orange600,
        tertiaryContainer = LightMint,
        onTertiaryContainer = DarkGreen,
        error = Orange600,
        errorContainer = LightCoral,
        onError = Color.White,
        onErrorContainer = Orange600,
        surfaceVariant = Cream,
        onSurfaceVariant = TextLight
    )
}

@Composable
fun CampusMenuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeName: String = "Turuncu",
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> getThemeDarkColors(themeName)
        else -> getThemeLightColors(themeName)
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}