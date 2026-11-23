package com.lakbay.tuklas.feature.discover.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lakbay.tuklas.feature.discover.domain.usecase.GetFeaturedPlacesUseCase
import com.lakbay.tuklas.feature.discover.domain.model.Place
import com.lakbay.tuklas.feature.discover.shared.mapper.filterPlacesForQuery
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val SEARCH_QUERY_KEY = "search_query"

class SearchViewModel(
    private val getFeaturedPlaces: GetFeaturedPlacesUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val query get() = savedStateHandle.get<String>(SEARCH_QUERY_KEY).orEmpty()
    private val _state = MutableStateFlow<SearchUiState>(SearchUiState.Loading(query))
    val state = _state.asStateFlow()
    private var loadedPlaces: List<Place> = emptyList()
    private var loadJob: Job? = null

    init { loadPlaces() }

    fun onIntent(intent: SearchIntent) {
        when (intent) {
            SearchIntent.Retry -> loadPlaces()
            is SearchIntent.QueryChanged -> {
                savedStateHandle[SEARCH_QUERY_KEY] = intent.query
                _state.value = when (_state.value) {
                    is SearchUiState.Loading -> SearchUiState.Loading(query)
                    is SearchUiState.Error -> SearchUiState.Error(query)
                    is SearchUiState.Content -> content()
                }
            }
        }
    }

    private fun content() = SearchUiState.Content(query,
        if (query.isBlank()) emptyList() else filterPlacesForQuery(loadedPlaces, query))

    private fun loadPlaces() {
        if (loadJob?.isActive == true) return
        loadJob = viewModelScope.launch {
            _state.value = SearchUiState.Loading(query)
            try {
                loadedPlaces = getFeaturedPlaces().toList()
                _state.value = content()
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                _state.value = SearchUiState.Error(query)
            }
        }
    }
}
