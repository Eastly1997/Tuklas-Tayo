package com.lakbay.tuklas.feature.discover.presentation.contract

import com.lakbay.tuklas.feature.discover.presentation.model.FeaturedShopUiModel

import com.lakbay.tuklas.core.domain.Place
import com.lakbay.tuklas.feature.discover.presentation.mapper.toFeaturedShopUiModel

sealed interface DiscoverUiState {
    data object Loading : DiscoverUiState
    data class Content(
        val places: List<Place>,
        val searchQuery: String = "",
        val featuredShops: List<FeaturedShopUiModel> = places.map { it.toFeaturedShopUiModel() },
    ) : DiscoverUiState
    data object Error : DiscoverUiState
}
