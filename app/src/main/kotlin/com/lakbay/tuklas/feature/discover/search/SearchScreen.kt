package com.lakbay.tuklas.feature.discover.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lakbay.tuklas.core.designsystem.component.TuklasButton
import com.lakbay.tuklas.core.designsystem.component.TuklasSearchField
import com.lakbay.tuklas.core.designsystem.theme.TuklasSpacing
import com.lakbay.tuklas.feature.discover.shared.component.ExploreShopCard

/** App Scaffold supplies chrome/system-bar insets; consume only the remaining IME inset here. */
@Composable
internal fun SearchScreen(
    state: SearchUiState,
    onIntent: (SearchIntent) -> Unit,
    onPlaceSelected: (String) -> Unit,
    searchFieldModifier: Modifier = Modifier,
) {
    Column(Modifier.fillMaxSize().imePadding()) {
        LazyColumn(Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(TuklasSpacing.large),
            verticalArrangement = Arrangement.spacedBy(TuklasSpacing.large)) {
            if (state.query.isBlank()) {
                item("prompt") {
                    Column(verticalArrangement = Arrangement.spacedBy(TuklasSpacing.small)) {
                        Text("What are you looking for?", style = MaterialTheme.typography.titleLarge)
                        Text("Search restaurants, cafés, and local places.", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            } else when (state) {
                is SearchUiState.Loading -> item("loading") { CircularProgressIndicator() }
                is SearchUiState.Error -> item("error") {
                    Column(verticalArrangement = Arrangement.spacedBy(TuklasSpacing.small)) {
                        Text("We couldn't load places. Please try again.")
                        TuklasButton(onClick = { onIntent(SearchIntent.Retry) }) { Text("Retry") }
                    }
                }
                is SearchUiState.Content -> {
                    if (state.places.isEmpty()) item("empty") { Text("No shops found") }
                    items(state.places, key = { it.id }) { place ->
                        ExploreShopCard(place, { onPlaceSelected(place.id) }, Modifier.fillMaxWidth())
                    }
                }
            }
        }
        TuklasSearchField(
            query = state.query,
            onQueryChange = { onIntent(SearchIntent.QueryChanged(it)) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = TuklasSpacing.large, vertical = TuklasSpacing.small)
                .then(searchFieldModifier),
        )
    }
}