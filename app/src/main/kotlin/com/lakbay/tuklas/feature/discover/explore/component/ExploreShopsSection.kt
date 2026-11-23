package com.lakbay.tuklas.feature.discover.explore.component
import com.lakbay.tuklas.feature.discover.shared.component.ExploreShopCard

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import com.lakbay.tuklas.core.designsystem.component.*
import com.lakbay.tuklas.core.designsystem.theme.TuklasSpacing
import com.lakbay.tuklas.domain.model.Place
import com.lakbay.tuklas.domain.model.PlaceCategory
import com.lakbay.tuklas.feature.discover.shared.mapper.displayLabel

/** Emits adjoining pieces of one visual surface into the caller's existing lazy list. */
fun LazyListScope.exploreShopsSection(
    places: List<Place>,
    selectedCategory: PlaceCategory?,
    onCategorySelected: (PlaceCategory?) -> Unit,
    onShopClick: (String) -> Unit,
) {
    item("explore_heading", contentType = "explore_header") {
        TuklasSectionContainer(
            modifier = Modifier.padding(horizontal = TuklasSpacing.large),
            part = TuklasSectionPart.Top,
        ) {
            ExploreShopsHeader(places.size, selectedCategory, onCategorySelected)
        }
    }
    if (places.isEmpty()) {
        item("explore_empty", contentType = "explore_empty") {
            TuklasSectionContainer(Modifier.padding(horizontal = TuklasSpacing.large),
                part = TuklasSectionPart.Bottom) { Text("No shops found.") }
        }
    } else {
        itemsIndexed(places, key = { _, place -> "place_${place.id}" }, contentType = { _, _ -> "explore_row" }) { index, place ->
            TuklasSectionContainer(
                modifier = Modifier.padding(horizontal = TuklasSpacing.large),
                part = if (index == places.lastIndex) TuklasSectionPart.Bottom else TuklasSectionPart.Middle,
            ) {
                ExploreShopCard(place, { onShopClick(place.id) }, Modifier.fillMaxWidth())
                if (index < places.lastIndex) Spacer(Modifier.height(TuklasSpacing.large))
            }
        }
    }
}

@Composable
private fun ExploreShopsHeader(
    count: Int,
    selectedCategory: PlaceCategory?,
    onCategorySelected: (PlaceCategory?) -> Unit,
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(TuklasSpacing.small)) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(TuklasSpacing.extraSmall)) {
            Text("Explore Shops", Modifier.semantics { heading() }, style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface)
            Text("$count ${if (count == 1) "place" else "places"}",
                style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
    Spacer(Modifier.height(TuklasSpacing.medium))
    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(TuklasSpacing.small)) {
        TuklasChip(onClick = { onCategorySelected(null) }, label = { Text("All") },
            type = TuklasChipType.Filter(selectedCategory == null, emphasized = true))
        PlaceCategory.entries.forEach { category ->
            TuklasChip(onClick = { onCategorySelected(category) }, label = { Text(category.displayLabel()) },
                type = TuklasChipType.Filter(selectedCategory == category, emphasized = true))
        }
    }
    Spacer(Modifier.height(TuklasSpacing.large))
}