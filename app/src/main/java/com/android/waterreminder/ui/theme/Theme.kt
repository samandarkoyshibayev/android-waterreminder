package com.android.waterreminder.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun WaterReminderTheme(
    themeMode: String = "System",
    content: @Composable () -> Unit
) {
    val systemInDarkTheme = isSystemInDarkTheme()

    val darkTheme = when (themeMode) {
        "Light" -> false
        "Dark" -> true
        else -> systemInDarkTheme
    }

    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(
        LocalExtendedColors provides extendedColors,
        LocalAppSpacing provides AppSpacing,
        LocalAppShapes provides AppShapes
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

val LocalExtendedColors = compositionLocalOf { LightExtendedColors }
val LocalAppSpacing = compositionLocalOf { AppSpacing }
val LocalAppShapes = compositionLocalOf { AppShapes }
