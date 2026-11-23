package com.lakbay.tuklas.designgallery.section

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.lakbay.tuklas.designgallery.GallerySection

@Composable
internal fun TypographyGallerySection() = GallerySection("Typography") {
    val samples = with(MaterialTheme.typography) { listOf(
        "Display Large" to displayLarge, "Display Medium" to displayMedium, "Display Small" to displaySmall,
        "Headline Large" to headlineLarge, "Headline Medium" to headlineMedium, "Headline Small" to headlineSmall,
        "Title Large" to titleLarge, "Title Medium" to titleMedium, "Title Small" to titleSmall,
        "Body Large" to bodyLarge, "Body Medium" to bodyMedium, "Body Small" to bodySmall,
        "Label Large" to labelLarge, "Label Medium" to labelMedium, "Label Small" to labelSmall,
    ) }
    samples.forEach { (role, style) -> Text(role, style = style) }
}
