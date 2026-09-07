package com.example.task_manager.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Purple60,
    onPrimary = TextOnPurple,
    primaryContainer = Purple95,
    onPrimaryContainer = Purple10,
    secondary = Purple40,
    onSecondary = TextOnPurple,
    secondaryContainer = Purple90,
    onSecondaryContainer = Purple20,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = SurfaceLight,
    onSurface = TextPrimary,
    surfaceVariant = Purple95,
    onSurfaceVariant = TextSecondary,
    error = AccentRed,
    outline = Purple90
)

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = Purple10,
    primaryContainer = Purple40,
    onPrimaryContainer = Purple95,
    secondary = Purple60,
    onSecondary = TextOnPurple,
    background = BackgroundDark,
    onBackground = Color(0xFFE8E3F5),
    surface = SurfaceDark,
    onSurface = Color(0xFFE8E3F5),
    surfaceVariant = Color(0xFF2D1F4E),
    onSurfaceVariant = Color(0xFFBBB3D4),
    error = AccentRed
)

@Composable
fun Task_ManagerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
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