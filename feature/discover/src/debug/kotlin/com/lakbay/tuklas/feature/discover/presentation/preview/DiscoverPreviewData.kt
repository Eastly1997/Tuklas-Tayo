package com.lakbay.tuklas.feature.discover.presentation.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.lakbay.tuklas.core.domain.Place
import com.lakbay.tuklas.core.domain.PlaceCategory
import com.lakbay.tuklas.feature.discover.presentation.contract.DiscoverUiState

/** Offline display fixtures only; no DI, ViewModel, backend or image request is needed. */
internal class DiscoverPreviewData : PreviewParameterProvider<DiscoverUiState> {
    override val values: Sequence<DiscoverUiState>
        get() {
            val places = listOf(
                Place("preview-cafe", "Sample Café", PlaceCategory.CAFE, "Sample location", "Coffee and cozy afternoons", city = "Sample City"),
                Place("preview-shop", "Sample Neighborhood Shop", PlaceCategory.LOCAL_BUSINESS, "Sample location", "", city = "Sample City"),
            )
            return sequenceOf(
                DiscoverUiState.Loading,
                DiscoverUiState.Content(places),
                DiscoverUiState.Content(emptyList()),
                DiscoverUiState.Error,
                DiscoverUiState.Content(places.take(1), searchQuery = "café", featuredShops = emptyList()),
            )
        }
}