package com.lakbay.tuklas.feature.discover.presentation.contract

sealed interface DiscoverIntent {
    data class SearchQueryChanged(val query: String) : DiscoverIntent
    data object Retry : DiscoverIntent
}
