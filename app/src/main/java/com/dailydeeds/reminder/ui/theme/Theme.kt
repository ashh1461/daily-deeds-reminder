package com.dailydeeds.reminder.ui.theme

import android.app.Activity
import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

internal val DarkColorScheme = darkColorScheme(
    primary = EmeraldSoftDark,
    onPrimary = EmeraldInk,
    primaryContainer = EmeraldContainerDark,
    onPrimaryContainer = EmeraldOnContainerDark,
    secondary = GoldBright,
    onSecondary = Color(0xFF261B00),
    tertiary = SuccessGreenDark,
    onTertiary = Color(0xFF052512),
    outlineVariant = CardBorderColorDark,
    background = BackgroundDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondaryDark
)

internal val LightColorScheme = lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = SurfaceLight,
    primaryContainer = EmeraldMint,
    onPrimaryContainer = EmeraldInk,
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

private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(30.dp)
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
                // The app draws edge to edge (enableEdgeToEdge in MainActivity), so the bars are transparent; only
                // their icon colour follows the theme chosen in the app, which can differ from the system's.
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = !darkTheme
                controller.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    // The layout direction follows the UI language (LocaleSupport sets it on the activity's configuration): right to
    // left for Arabic and Persian, left to right for English. Religious text keeps RTL through [RtlContent].
    val rtl = LocalConfiguration.current.layoutDirection == View.LAYOUT_DIRECTION_RTL
    CompositionLocalProvider(LocalLayoutDirection provides if (rtl) LayoutDirection.Rtl else LayoutDirection.Ltr) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = AppShapes,
            content = content
        )
    }
}

/**
 * Religious text (Quran, supplications, Mafatih, Sahifa, al-Mizan) is Arabic content, never translated, so it stays
 * right to left even when the UI is in English.
 */
@Composable
fun RtlContent(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl, content = content)
}
