package com.lakbay.tuklas.feature.explore

import androidx.compose.runtime.Composable
import com.lakbay.tuklas.feature.explore.presentation.ExploreScreen
import com.lakbay.tuklas.feature.explore.presentation.contract.ExploreUiState

@Composable
fun ExploreRoute(onBack: () -> Unit) {
    ExploreScreen(state = ExploreUiState(), onBack = onBack)
}