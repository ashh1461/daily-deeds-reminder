package com.dailydeeds.reminder.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import org.junit.Assert.assertTrue
import org.junit.Test

class ColorSchemeContrastTest {
    @Test
    fun textRolesMeetContrastInBothThemes() {
        listOf(LightColorScheme, DarkColorScheme).forEachIndexed { index, scheme ->
            val pairs = mapOf(
                "surface" to (scheme.onSurface to scheme.surface),
                "surface variant" to (scheme.onSurfaceVariant to scheme.surfaceVariant),
                "primary" to (scheme.onPrimary to scheme.primary),
                "primary container" to (scheme.onPrimaryContainer to scheme.primaryContainer),
                "secondary" to (scheme.onSecondary to scheme.secondary),
                "tertiary" to (scheme.onTertiary to scheme.tertiary),
                "primary text" to (scheme.primary to scheme.surface),
                "secondary text" to (scheme.secondary to scheme.surface),
                "completion text" to (scheme.tertiary to scheme.surface)
            )
            pairs.forEach { (role, colors) ->
                val contrast = contrast(colors.first, colors.second)
                assertTrue("Theme $index, $role contrast: $contrast", contrast >= 4.5f)
            }
        }
    }

    private fun contrast(foreground: Color, background: Color): Float {
        val first = foreground.luminance()
        val second = background.luminance()
        return (maxOf(first, second) + 0.05f) / (minOf(first, second) + 0.05f)
    }
}
