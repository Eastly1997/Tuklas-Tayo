package com.lakbay.tuklas.feature.discover.explore.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.lakbay.tuklas.feature.discover.explore.AnnouncementUiModel
import com.lakbay.tuklas.feature.discover.domain.model.Place
import com.lakbay.tuklas.feature.discover.domain.model.PlaceCategory
import com.lakbay.tuklas.feature.discover.explore.ExploreUiState

/** Offline display fixtures only; no DI, ViewModel, backend or image request is needed. */
internal class ExplorePreviewData : PreviewParameterProvider<ExploreUiState> {
    override val values: Sequence<ExploreUiState>
        get() {
            val places = listOf(
                Place("preview-cafe", "Sample Café", PlaceCategory.CAFE, "Sample location", "Coffee and cozy afternoons", city = "Sample City"),
                Place("preview-shop", "Sample Neighborhood Shop", PlaceCategory.LOCAL_BUSINESS, "Sample location", "", city = "Sample City"),
            )
            val announcement = AnnouncementUiModel("preview-welcome", "Welcome to TuklasTayo!",
                "Discover local favorites around Malolos.")
            return sequenceOf(
                ExploreUiState.Loading,
                ExploreUiState.Content(places, announcement = announcement),
                ExploreUiState.Content(emptyList()),
                ExploreUiState.Error,
                ExploreUiState.Content(places.take(1), searchQuery = "café", featuredShops = emptyList(), announcement = announcement),
            )
        }
}