package com.lakbay.tuklas.feature.discover.explore

import com.lakbay.tuklas.feature.discover.explore.component.FeaturedShopsSection
import com.lakbay.tuklas.feature.discover.explore.component.rememberFeaturedShopsCarouselState

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lakbay.tuklas.core.designsystem.component.TuklasAnnouncementBanner
import com.lakbay.tuklas.core.designsystem.component.TuklasButton
import com.lakbay.tuklas.core.designsystem.component.TuklasSearchTrigger
import com.lakbay.tuklas.feature.discover.explore.component.exploreShopsSection
import com.lakbay.tuklas.core.designsystem.theme.TuklasSpacing

@Composable
internal fun ExploreScreen(
    state: ExploreUiState,
    onIntent: (ExploreIntent) -> Unit,
    onPlaceSelected: (String) -> Unit,
    developmentNotice: String? = null,
    onAnnouncementSelected: ((AnnouncementDestination) -> Unit)? = null,
    onSearch: () -> Unit = {},
) {
    val content = state as? ExploreUiState.Content
    val featuredCarouselState = rememberFeaturedShopsCarouselState()
    LazyColumn(
        state = rememberLazyListState(),
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainer),
        contentPadding = PaddingValues(vertical = TuklasSpacing.large),
        verticalArrangement = Arrangement.Top,
    ) {
        item("search") {
            TuklasSearchTrigger(
                onClick = onSearch,
                modifier = Modifier.fillMaxWidth().padding(horizontal = TuklasSpacing.large).padding(bottom = TuklasSpacing.large),
            )
        }
        if (developmentNotice != null && content?.searchQuery.isNullOrBlank()) item("development_notice") {
            Text(developmentNotice, Modifier.padding(horizontal = TuklasSpacing.large).padding(bottom = TuklasSpacing.large), style = MaterialTheme.typography.bodySmall)
        }
        content?.visibleAnnouncement?.let { announcement ->
            item("announcement") {
                val destination = announcement.destination
                val handler = onAnnouncementSelected
                TuklasAnnouncementBanner(
                    title = announcement.title,
                    description = announcement.description,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = TuklasSpacing.large).padding(bottom = TuklasSpacing.large),
                    onClick = if (destination != null && handler != null) {
                        { handler(destination) }
                    } else null,
                )
            }
        }
        when (state) {
            ExploreUiState.Loading -> item("loading") { CircularProgressIndicator(Modifier.padding(horizontal = TuklasSpacing.large).padding(bottom = TuklasSpacing.large)) }
            ExploreUiState.Error -> item("error") {
                Column(Modifier.padding(horizontal = TuklasSpacing.large).padding(bottom = TuklasSpacing.large), verticalArrangement = Arrangement.spacedBy(TuklasSpacing.small)) {
                    Text("We couldn't load places. Please try again.")
                    TuklasButton(onClick = { onIntent(ExploreIntent.Retry) }) { Text("Retry") }
                }
            }
            is ExploreUiState.Content -> {
                if (state.featuredShops.isNotEmpty())
                    item("featured") {
                        FeaturedShopsSection(state.featuredShops, onPlaceSelected,
                            modifier = Modifier.padding(bottom = TuklasSpacing.large), carouselState = featuredCarouselState)
                    }
                exploreShopsSection(
                    places = state.places,
                    selectedCategory = state.selectedCategory,
                    onCategorySelected = { onIntent(ExploreIntent.CategorySelected(it)) },
                    onShopClick = onPlaceSelected,
                )
            }
        }
    }
}