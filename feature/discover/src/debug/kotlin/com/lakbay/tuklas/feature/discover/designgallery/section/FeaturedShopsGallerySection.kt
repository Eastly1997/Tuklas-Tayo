package com.lakbay.tuklas.feature.discover.designgallery.section

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.lakbay.tuklas.feature.discover.R
import com.lakbay.tuklas.feature.discover.development.developmentPlaces
import com.lakbay.tuklas.feature.discover.designgallery.GallerySection
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme
import com.lakbay.tuklas.feature.discover.shared.component.ExploreShopCard
import com.lakbay.tuklas.feature.discover.explore.component.FeaturedShopCard
import com.lakbay.tuklas.feature.discover.explore.component.FeaturedShopsSection
import com.lakbay.tuklas.feature.discover.shared.mapper.toFeaturedShopUiModel
import com.lakbay.tuklas.feature.discover.shared.model.FeaturedShopUiModel
import com.lakbay.tuklas.core.designsystem.theme.TuklasSpacing

private val samples = developmentPlaces.map { place ->
    place.toFeaturedShopUiModel().copy(imageResource = when (place.id) {
        "sample-kainan" -> R.drawable.featured_sample_cafe
        "sample-kape" -> R.drawable.featured_sample_coffee
        else -> null
    })
}

internal data class FeaturedShopExample(val label: String, val shop: FeaturedShopUiModel)

private val examples = listOf(
    FeaturedShopExample("Standard • lighter café photo", samples.first().copy(
        name = "Sample Café", highlight = "Coffee and cozy afternoons")),
    FeaturedShopExample("Long shop name • darker coffee photo", samples[1].copy(
        name = "Sample Neighborhood Coffee House and Community Kitchen", highlight = "Fresh coffee daily")),
    FeaturedShopExample("Long highlight", samples.first().copy(
        highlight = "Freshly brewed coffee and comforting neighborhood favorites for a relaxed afternoon")),
    FeaturedShopExample("Missing highlight", samples[1].copy(highlight = null)),
    FeaturedShopExample("Missing image", samples.last().copy(highlight = "A fictional local shop")),
    FeaturedShopExample("Long name and highlight together", samples.first().copy(
        name = "Sample Neighborhood Coffee House and Community Kitchen",
        highlight = "Freshly brewed coffee and comforting neighborhood favorites for a relaxed afternoon")),
)

@Composable
internal fun FeaturedShopsGallerySection(onAction: (String) -> Unit) {
    GallerySection("Featured shop components") {
        Text("Fictional development samples • stock photographs do not depict these businesses.")
        Text("Static centered carousel • swipe to inspect neighboring cards")
        FeaturedShopsSection(samples, { onAction("Selected sample: $it") }, autoScrollEnabled = false)
        Text("Interactive autoplay • six seconds • heading accessibility action pauses/resumes")
        FeaturedShopsSection(samples, { onAction("Selected sample: $it") }, autoScrollEnabled = true)
        examples.forEach { example ->
            Text(example.label)
            FeaturedShopCard(example.shop, { onAction("Selected sample: ${example.shop.id}") }, Modifier.fillMaxWidth())
        }
        Text("Shared Explore / Search result card • fictional development sample")
        Column(verticalArrangement = Arrangement.spacedBy(TuklasSpacing.large)) {
            developmentPlaces.take(2).forEach { place ->
                ExploreShopCard(place, { onAction("Selected sample: ${place.id}") }, Modifier.fillMaxWidth())
            }
        }
        Text("One-item section")
        FeaturedShopsSection(samples.take(1), { onAction("Selected sample: $it") }, autoScrollEnabled = false)
    }
}

internal class FeaturedShopExamples : PreviewParameterProvider<FeaturedShopExample> {
    override val values = examples.asSequence()
}

@Preview(name = "Featured Card Light", showBackground = true, widthDp = 360, heightDp = 400)
@Preview(name = "Featured Card Dark", showBackground = true, widthDp = 360, heightDp = 400,
    uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Featured Card Large text", showBackground = true, widthDp = 360, heightDp = 500, fontScale = 2f)
@Composable
private fun FeaturedShopPreview(@PreviewParameter(FeaturedShopExamples::class) example: FeaturedShopExample) {
    TuklasTheme(dynamicColor = false) {
        Column(Modifier.padding(TuklasSpacing.large), verticalArrangement = Arrangement.spacedBy(TuklasSpacing.small)) {
            Text("Development sample • ${example.label}")
            FeaturedShopCard(example.shop, {}, Modifier.fillMaxWidth())
        }
    }
}

@Preview(name = "Featured Carousel", showBackground = true, widthDp = 400, heightDp = 400)
@Composable
private fun FeaturedShopsPreview() {
    TuklasTheme(dynamicColor = false) {
        Column {
            Text("Fictional development samples", Modifier.padding(horizontal = TuklasSpacing.large))
            FeaturedShopsSection(samples, {}, autoScrollEnabled = false)
        }
    }
}