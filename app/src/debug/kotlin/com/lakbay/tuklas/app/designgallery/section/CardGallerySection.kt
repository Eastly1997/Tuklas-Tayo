package com.lakbay.tuklas.app.designgallery.section

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lakbay.tuklas.app.designgallery.GallerySection
import com.lakbay.tuklas.core.designsystem.component.TuklasCard
import com.lakbay.tuklas.core.designsystem.component.TuklasCardVariant
import com.lakbay.tuklas.core.designsystem.theme.TuklasSpacing

@Composable
internal fun CardGallerySection(onAction: (String) -> Unit) = GallerySection("Cards") {
    TuklasCardVariant.entries.forEach { variant ->
        TuklasCard(Modifier.fillMaxWidth(), variant, onClick = { onAction("${variant.name} card clicked") }) {
            Text("${variant.name} interactive card", Modifier.padding(TuklasSpacing.large))
        }
    }
    TuklasCard(Modifier.fillMaxWidth()) {
        Text("Static container • no click action", Modifier.padding(TuklasSpacing.large))
    }
    TuklasCard(Modifier.fillMaxWidth(), TuklasCardVariant.OUTLINED, onClick = {}, enabled = false) {
        Text("Disabled interactive card", Modifier.padding(TuklasSpacing.large))
    }
}
