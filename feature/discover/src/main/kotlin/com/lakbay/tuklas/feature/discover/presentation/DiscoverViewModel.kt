package com.lakbay.tuklas.feature.discover.presentation

import com.lakbay.tuklas.feature.discover.presentation.mapper.toFeaturedShopUiModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lakbay.tuklas.core.domain.GetFeaturedPlacesUseCase
import com.lakbay.tuklas.feature.discover.presentation.contract.DiscoverIntent
import com.lakbay.tuklas.feature.discover.presentation.contract.DiscoverUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DiscoverViewModel(private val getFeaturedPlaces: GetFeaturedPlacesUseCase) : ViewModel() {
    private val _state = MutableStateFlow<DiscoverUiState>(DiscoverUiState.Loading)
    val state = _state.asStateFlow()
    private var loadJob: Job? = null

    private var loadedPlaces = emptyList<com.lakbay.tuklas.core.domain.Place>()
    private var searchQuery = ""

    init { loadPlaces() }

    fun onIntent(intent: DiscoverIntent) {
        when (intent) {
            DiscoverIntent.Retry -> loadPlaces()
            is DiscoverIntent.SearchQueryChanged -> {
                searchQuery = intent.query
                if (_state.value is DiscoverUiState.Content) publishContent()
            }
        }
    }

    private fun publishContent() {
        val query = searchQuery.trim()
        val results = if (query.isBlank()) loadedPlaces else loadedPlaces.filter {
            it.name.contains(query, ignoreCase = true) || it.address.contains(query, ignoreCase = true) ||
                it.category.name.replace('_', ' ').contains(query, ignoreCase = true) ||
                it.toFeaturedShopUiModel().category.contains(query, ignoreCase = true)
        }
        _state.value = DiscoverUiState.Content(
            places = results, searchQuery = searchQuery,
            featuredShops = if (query.isBlank()) loadedPlaces.map { it.toFeaturedShopUiModel() } else emptyList(),
        )
    }

    private fun loadPlaces() {
        if (loadJob?.isActive == true) return
        loadJob = viewModelScope.launch {
            _state.value = DiscoverUiState.Loading
            try {
                loadedPlaces = getFeaturedPlaces().toList()
                publishContent()
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                _state.value = DiscoverUiState.Error
            }
        }
    }
}
