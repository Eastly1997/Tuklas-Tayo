package com.lakbay.tuklas.feature.discover.designgallery.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lakbay.tuklas.feature.discover.designgallery.GallerySection
import com.lakbay.tuklas.core.designsystem.theme.TuklasSpacing

@Composable
internal fun ShapeGallerySection() = GallerySection("Shapes & spacing") {
    val shapes = with(MaterialTheme.shapes) { listOf(
        "Extra small" to extraSmall, "Small" to small, "Medium" to medium,
        "Large" to large, "Extra large" to extraLarge,
    ) }
    shapes.forEach { (name, shape) ->
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(TuklasSpacing.large)) {
            Box(Modifier.size(72.dp).background(MaterialTheme.colorScheme.surfaceContainerHigh, shape))
            Text(name, style = MaterialTheme.typography.labelLarge)
        }
    }
    val spacing = with(TuklasSpacing) { listOf(
        "Extra small" to extraSmall, "Small" to small, "Medium" to medium,
        "Large" to large, "Extra large" to extraLarge, "Extra extra large" to extraExtraLarge,
    ) }
    spacing.forEach { (name, size) ->
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(TuklasSpacing.large)) {
            Box(Modifier.width(size).height(TuklasSpacing.large).background(MaterialTheme.colorScheme.primary))
            Text("$name: ${size.value.toInt()}dp", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
