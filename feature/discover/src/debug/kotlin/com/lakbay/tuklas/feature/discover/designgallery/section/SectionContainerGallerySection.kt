package com.lakbay.tuklas.feature.discover.designgallery.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lakbay.tuklas.feature.discover.designgallery.GallerySection
import com.lakbay.tuklas.core.designsystem.component.TuklasSectionContainer
import com.lakbay.tuklas.core.designsystem.theme.TuklasSpacing

@Composable
internal fun SectionContainerGallerySection() {
    GallerySection("Grouped section containers") {
        Column(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(TuklasSpacing.large),
            verticalArrangement = Arrangement.spacedBy(TuklasSpacing.large)) {
            TuklasSectionContainer { Text("Heading", style = MaterialTheme.typography.titleLarge) }
            TuklasSectionContainer {
                Column(verticalArrangement = Arrangement.spacedBy(TuklasSpacing.large)) {
                    Text("Multiple content items", style = MaterialTheme.typography.titleLarge)
                    Text("First item")
                    Text("Second item")
                }
            }
            TuklasSectionContainer {
                Text("A longer section heading that can wrap", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(TuklasSpacing.medium))
                Text("Content-driven surfaces accommodate longer text and larger fonts without fixed-height clipping.")
            }
        }
        Text("Actual grouped Explore Shops • fictional development data • interactive category filters")
    }
}