package com.lakbay.tuklas.feature.discover.explore

import com.lakbay.tuklas.domain.model.PlaceCategory

sealed interface ExploreIntent {
    data class CategorySelected(val category: PlaceCategory?) : ExploreIntent
    data class SearchQueryChanged(val query: String) : ExploreIntent
    data object Retry : ExploreIntent
}
