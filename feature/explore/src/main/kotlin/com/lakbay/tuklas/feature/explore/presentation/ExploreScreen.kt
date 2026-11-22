package com.lakbay.tuklas.feature.explore.presentation

import androidx.compose.runtime.Composable
import com.lakbay.tuklas.core.designsystem.PlaceholderScreen
import com.lakbay.tuklas.feature.explore.presentation.contract.ExploreUiState

@Composable
internal fun ExploreScreen(state: ExploreUiState, onBack: () -> Unit) {
    PlaceholderScreen("Explore", state.message, onBack)
}