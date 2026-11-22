package com.lakbay.tuklas.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily

private val materialTypography = Typography()

// Keep the complete standard Material 3 sp scale, line heights, weights and letter spacing.
// Only the family is explicit: platform sans-serif, with no downloaded or bundled font.
internal val tuklasTypography = Typography(
    displayLarge = materialTypography.displayLarge.copy(fontFamily = FontFamily.SansSerif),
    displayMedium = materialTypography.displayMedium.copy(fontFamily = FontFamily.SansSerif),
    displaySmall = materialTypography.displaySmall.copy(fontFamily = FontFamily.SansSerif),
    headlineLarge = materialTypography.headlineLarge.copy(fontFamily = FontFamily.SansSerif),
    headlineMedium = materialTypography.headlineMedium.copy(fontFamily = FontFamily.SansSerif),
    headlineSmall = materialTypography.headlineSmall.copy(fontFamily = FontFamily.SansSerif),
    titleLarge = materialTypography.titleLarge.copy(fontFamily = FontFamily.SansSerif),
    titleMedium = materialTypography.titleMedium.copy(fontFamily = FontFamily.SansSerif),
    titleSmall = materialTypography.titleSmall.copy(fontFamily = FontFamily.SansSerif),
    bodyLarge = materialTypography.bodyLarge.copy(fontFamily = FontFamily.SansSerif),
    bodyMedium = materialTypography.bodyMedium.copy(fontFamily = FontFamily.SansSerif),
    bodySmall = materialTypography.bodySmall.copy(fontFamily = FontFamily.SansSerif),
    labelLarge = materialTypography.labelLarge.copy(fontFamily = FontFamily.SansSerif),
    labelMedium = materialTypography.labelMedium.copy(fontFamily = FontFamily.SansSerif),
    labelSmall = materialTypography.labelSmall.copy(fontFamily = FontFamily.SansSerif),
)
