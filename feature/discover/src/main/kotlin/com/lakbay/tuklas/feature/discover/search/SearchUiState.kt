package com.lakbay.tuklas.feature.discover.search

import com.lakbay.tuklas.feature.discover.domain.model.Place

sealed interface SearchUiState {
    val query: String
    data class Loading(override val query: String = "") : SearchUiState
    data class Content(override val query: String, val places: List<Place>) : SearchUiState
    data class Error(override val query: String) : SearchUiState
}