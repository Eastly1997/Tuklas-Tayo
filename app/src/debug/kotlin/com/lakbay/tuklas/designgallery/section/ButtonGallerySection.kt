package com.lakbay.tuklas.designgallery.section

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.lakbay.tuklas.designgallery.GallerySection
import com.lakbay.tuklas.core.designsystem.component.TuklasButton
import com.lakbay.tuklas.core.designsystem.component.TuklasButtonVariant

@Composable
internal fun ButtonGallerySection(onAction: (String) -> Unit) = GallerySection("Buttons") {
    TuklasButtonVariant.entries.forEach { variant ->
        val name = variant.name.lowercase().replace('_', ' ').replaceFirstChar { it.titlecase() }
        TuklasButton(onClick = { onAction("$name button clicked") }, variant = variant) { Text(name) }
        TuklasButton(onClick = {}, variant = variant, enabled = false) { Text("$name disabled") }
    }
}
