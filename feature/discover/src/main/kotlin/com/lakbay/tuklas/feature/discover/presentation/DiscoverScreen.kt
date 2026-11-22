package com.lakbay.tuklas.feature.discover.presentation

import com.lakbay.tuklas.feature.discover.presentation.component.FeaturedShopsSection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import com.lakbay.tuklas.core.designsystem.component.TuklasButton
import com.lakbay.tuklas.core.designsystem.component.TuklasTextField
import com.lakbay.tuklas.core.designsystem.theme.TuklasSpacing
import com.lakbay.tuklas.feature.discover.presentation.contract.DiscoverIntent
import com.lakbay.tuklas.feature.discover.presentation.contract.DiscoverUiState

@Composable
internal fun DiscoverScreen(
    state: DiscoverUiState,
    onIntent: (DiscoverIntent) -> Unit,
    onExplore: () -> Unit,
    onPlaceSelected: (String) -> Unit,
    developmentNotice: String? = null,
) {
    val content = state as? DiscoverUiState.Content
    LazyColumn(
        state = rememberLazyListState(),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(TuklasSpacing.large),
        verticalArrangement = Arrangement.spacedBy(TuklasSpacing.large),
    ) {
        item("search") {
            TuklasTextField(
                value = content?.searchQuery.orEmpty(),
                onValueChange = { onIntent(DiscoverIntent.SearchQueryChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Search shops") },
                placeholder = { Text("Name, category or location") },
                enabled = content != null,
            )
        }
        if (developmentNotice != null) item("development_notice") {
            Text(developmentNotice, style = MaterialTheme.typography.bodySmall)
        }
        when (state) {
            DiscoverUiState.Loading -> item("loading") { CircularProgressIndicator() }
            DiscoverUiState.Error -> item("error") {
                Column(verticalArrangement = Arrangement.spacedBy(TuklasSpacing.small)) {
                    Text("We couldn't load places. Please try again.")
                    TuklasButton(onClick = { onIntent(DiscoverIntent.Retry) }) { Text("Retry") }
                }
            }
            is DiscoverUiState.Content -> {
                if (state.featuredShops.isNotEmpty()) item("featured") {
                    FeaturedShopsSection(state.featuredShops, onPlaceSelected)
                }
                item("explore_heading") {
                    Column(verticalArrangement = Arrangement.spacedBy(TuklasSpacing.small)) {
                        Text("Explore Shops", Modifier.semantics { heading() }, style = MaterialTheme.typography.titleLarge)
                        TuklasButton(onClick = onExplore) { Text("Explore") }
                    }
                }
                if (state.places.isEmpty()) item("empty") { Text("No shops found.") }
                items(state.places, key = { "place_${it.id}" }) { place ->
                    Column(Modifier.fillMaxWidth().clickable { onPlaceSelected(place.id) }.padding(TuklasSpacing.large)) {
                        Text(place.name, style = MaterialTheme.typography.titleMedium)
                        Text(place.address)
                    }
                }
            }
        }
    }
}