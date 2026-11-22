package com.lakbay.tuklas.feature.placedetail.presentation

import androidx.compose.runtime.Composable
import com.lakbay.tuklas.core.designsystem.PlaceholderScreen
import com.lakbay.tuklas.feature.placedetail.presentation.contract.PlaceDetailUiState

@Composable
internal fun PlaceDetailScreen(state: PlaceDetailUiState, onBack: () -> Unit) {
    PlaceholderScreen("Place Detail", "Selected place: ${state.placeId}\nDetails are planned for a later milestone.", onBack)
}