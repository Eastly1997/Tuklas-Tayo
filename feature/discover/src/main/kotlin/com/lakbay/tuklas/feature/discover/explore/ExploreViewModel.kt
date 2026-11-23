package com.lakbay.tuklas.feature.discover.explore

import com.lakbay.tuklas.feature.discover.shared.mapper.toFeaturedShopUiModel

import com.lakbay.tuklas.feature.discover.shared.mapper.filterPlacesForQuery
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lakbay.tuklas.feature.discover.domain.usecase.GetFeaturedPlacesUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExploreViewModel(
    private val getFeaturedPlaces: GetFeaturedPlacesUseCase,
    private val initialAnnouncement: AnnouncementUiModel? = null,
) : ViewModel() {
    private val _state = MutableStateFlow<ExploreUiState>(ExploreUiState.Loading)
    val state = _state.asStateFlow()
    private var loadJob: Job? = null

    private var loadedPlaces = emptyList<com.lakbay.tuklas.feature.discover.domain.model.Place>()
    private var searchQuery = ""
    private var selectedCategory: com.lakbay.tuklas.feature.discover.domain.model.PlaceCategory? = null

    init { loadPlaces() }

    fun onIntent(intent: ExploreIntent) {
        when (intent) {
            ExploreIntent.Retry -> loadPlaces()
            is ExploreIntent.CategorySelected -> {
                selectedCategory = intent.category
                if (_state.value is ExploreUiState.Content) publishContent()
            }
            is ExploreIntent.SearchQueryChanged -> {
                searchQuery = intent.query
                if (_state.value is ExploreUiState.Content) publishContent()
            }
        }
    }

    private fun publishContent() {
        val query = searchQuery.trim()
        val results = filterPlacesForQuery(loadedPlaces, query).filter {
            selectedCategory == null || it.category == selectedCategory
        }
        _state.value = ExploreUiState.Content(
            places = results, searchQuery = searchQuery, selectedCategory = selectedCategory,
            announcement = initialAnnouncement,
            featuredShops = if (query.isBlank()) loadedPlaces.map { it.toFeaturedShopUiModel() } else emptyList(),
        )
    }

    private fun loadPlaces() {
        if (loadJob?.isActive == true) return
        loadJob = viewModelScope.launch {
            _state.value = ExploreUiState.Loading
            try {
                loadedPlaces = getFeaturedPlaces().toList()
                publishContent()
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                _state.value = ExploreUiState.Error
            }
        }
    }
}
