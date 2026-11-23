package com.lakbay.tuklas.feature.discover.explore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExploreRoute(onPlaceSelected: (String) -> Unit, onSearch: () -> Unit, developmentNotice: String? = null) {
    val viewModel: ExploreViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    ExploreScreen(state, viewModel::onIntent, onPlaceSelected, developmentNotice,
        onSearch = onSearch,
        onAnnouncementSelected = { destination ->
            when (destination) {
                is AnnouncementDestination.PlaceDetail -> onPlaceSelected(destination.placeId)
            }
        },
    )
}
