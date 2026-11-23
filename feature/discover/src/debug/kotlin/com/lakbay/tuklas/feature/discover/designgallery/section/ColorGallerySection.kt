package com.lakbay.tuklas.feature.discover.designgallery.section

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.lakbay.tuklas.feature.discover.designgallery.GallerySection
import com.lakbay.tuklas.core.designsystem.theme.TuklasSpacing

@Composable
internal fun ColorGallerySection() = GallerySection("Colors") {
    val colors = MaterialTheme.colorScheme
    val swatches = with(colors) { listOf(
        "Primary" to primary, "OnPrimary" to onPrimary, "PrimaryContainer" to primaryContainer,
        "OnPrimaryContainer" to onPrimaryContainer, "InversePrimary" to inversePrimary,
        "Secondary" to secondary, "OnSecondary" to onSecondary,
        "SecondaryContainer" to secondaryContainer, "OnSecondaryContainer" to onSecondaryContainer,
        "Tertiary" to tertiary, "OnTertiary" to onTertiary,
        "TertiaryContainer" to tertiaryContainer, "OnTertiaryContainer" to onTertiaryContainer,
        "Background" to background, "OnBackground" to onBackground,
        "Surface" to surface, "OnSurface" to onSurface, "SurfaceVariant" to surfaceVariant,
        "OnSurfaceVariant" to onSurfaceVariant, "SurfaceTint" to surfaceTint,
        "SurfaceDim" to surfaceDim, "SurfaceBright" to surfaceBright,
        "SurfaceContainerLowest" to surfaceContainerLowest, "SurfaceContainerLow" to surfaceContainerLow,
        "SurfaceContainer" to surfaceContainer, "SurfaceContainerHigh" to surfaceContainerHigh,
        "SurfaceContainerHighest" to surfaceContainerHighest,
        "InverseSurface" to inverseSurface, "InverseOnSurface" to inverseOnSurface,
        "Error" to error, "OnError" to onError, "ErrorContainer" to errorContainer,
        "OnErrorContainer" to onErrorContainer, "Outline" to outline, "OutlineVariant" to outlineVariant,
        "Scrim" to scrim,
    ) }
    swatches.forEach { (role, color) ->
        val argb = color.toArgb()
        val digits = argb.toUInt().toString(16).uppercase().padStart(8, '0')
        val hex = "#" + if ((argb ushr 24) == 255) digits.takeLast(6) else digits
        Row(horizontalArrangement = Arrangement.spacedBy(TuklasSpacing.medium)) {
            Box(Modifier.size(48.dp).background(color, MaterialTheme.shapes.small)
                .border(1.dp, colors.outline, MaterialTheme.shapes.small))
            Column {
                Text(role, style = MaterialTheme.typography.labelLarge)
                Text(hex, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
