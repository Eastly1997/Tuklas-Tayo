package com.lakbay.tuklas.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import org.junit.Assert.assertTrue
import org.junit.Test

class ThemeContrastTest {
    @Test fun `light semantic text pairs meet normal text contrast`() {
        checkTextContrast(tuklasLightColorScheme)
    }

    @Test fun `dark semantic text pairs meet normal text contrast`() {
        checkTextContrast(tuklasDarkColorScheme)
    }

    @Test fun `interactive outlines contrast against text field surfaces`() {
        listOf(tuklasLightColorScheme, tuklasDarkColorScheme).forEach { scheme ->
            assertContrast("outline on surface", scheme.outline, scheme.surface, minimum = 3f)
            assertContrast("outline on container", scheme.outline, scheme.surfaceContainer, minimum = 3f)
        }
    }

    private fun checkTextContrast(scheme: ColorScheme) = with(scheme) {
        listOf(
            Triple("primary", onPrimary, primary),
            Triple("primary container", onPrimaryContainer, primaryContainer),
            Triple("secondary", onSecondary, secondary),
            Triple("secondary container", onSecondaryContainer, secondaryContainer),
            Triple("tertiary", onTertiary, tertiary),
            Triple("tertiary container", onTertiaryContainer, tertiaryContainer),
            Triple("error", onError, error),
            Triple("error container", onErrorContainer, errorContainer),
            Triple("background", onBackground, background),
            Triple("surface variant", onSurfaceVariant, surfaceVariant),
            Triple("inverse surface", inverseOnSurface, inverseSurface),
            Triple("inverse primary", inversePrimary, inverseSurface),
        ).forEach { (role, foreground, background) -> assertContrast(role, foreground, background) }
        listOf(surface, surfaceBright, surfaceDim, surfaceContainerLowest, surfaceContainerLow,
            surfaceContainer, surfaceContainerHigh, surfaceContainerHighest).forEach { container ->
            assertContrast("surface text", onSurface, container)
            assertContrast("supporting surface text", onSurfaceVariant, container)
        }
    }

    private fun assertContrast(role: String, foreground: Color, background: Color, minimum: Float = 4.5f) {
        val first = foreground.luminance()
        val second = background.luminance()
        val ratio = (maxOf(first, second) + 0.05f) / (minOf(first, second) + 0.05f)
        assertTrue("$role contrast $ratio must be at least $minimum:1", ratio >= minimum)
    }
}
