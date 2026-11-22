package com.lakbay.tuklas.feature.discover.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun DiscoverRoute(onExplore: () -> Unit, onPlaceSelected: (String) -> Unit, developmentNotice: String? = null) {
    val viewModel: DiscoverViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    DiscoverScreen(state, viewModel::onIntent, onExplore, onPlaceSelected, developmentNotice)
}
