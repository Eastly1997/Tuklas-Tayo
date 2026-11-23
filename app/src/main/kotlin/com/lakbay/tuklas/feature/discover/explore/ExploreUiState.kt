package com.lakbay.tuklas.feature.discover.explore

import com.lakbay.tuklas.feature.discover.shared.model.FeaturedShopUiModel

import com.lakbay.tuklas.domain.model.Place
import com.lakbay.tuklas.feature.discover.shared.mapper.toFeaturedShopUiModel

import com.lakbay.tuklas.domain.model.PlaceCategory

sealed interface ExploreUiState {
    data object Loading : ExploreUiState
    data class Content(
        val places: List<Place>,
        val searchQuery: String = "",
        val featuredShops: List<FeaturedShopUiModel> = places.map { it.toFeaturedShopUiModel() },
        val announcement: AnnouncementUiModel? = null,
        val selectedCategory: PlaceCategory? = null,
    ) : ExploreUiState {
        val visibleAnnouncement: AnnouncementUiModel?
            get() = announcement?.takeIf { it.isActive && searchQuery.isBlank() }
    }
    data object Error : ExploreUiState
}
