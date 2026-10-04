package com.dailydeeds.reminder.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.view.WindowCompat

internal val DarkColorScheme = darkColorScheme(
    primary = NavyLight,
    onPrimary = BackgroundDark,
    primaryContainer = NavySecondary,
    onPrimaryContainer = NavyLight,
    secondary = GoldLight,
    onSecondary = BackgroundDark,
    tertiary = SuccessGreenContainer,
    onTertiary = BackgroundDark,
    outlineVariant = CardBorderColorDark,
    background = BackgroundDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondaryDark
)

internal val LightColorScheme = lightColorScheme(
    primary = NavyPrimary,
    onPrimary = SurfaceLight,
    primaryContainer = NavyLight,
    onPrimaryContainer = NavyDark,
    secondary = GoldText,
    onSecondary = SurfaceLight,
    tertiary = SuccessGreen,
    onTertiary = SurfaceLight,
    outlineVariant = CardBorderColorLight,
    background = BackgroundLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondaryLight
)

@Composable
fun DailyReminderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.surface.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
