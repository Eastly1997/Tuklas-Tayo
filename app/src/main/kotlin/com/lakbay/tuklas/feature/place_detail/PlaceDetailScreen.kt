package com.lakbay.tuklas.feature.place_detail

import androidx.compose.runtime.Composable
import com.lakbay.tuklas.core.designsystem.PlaceholderScreen

@Composable
internal fun PlaceDetailScreen(state: PlaceDetailUiState, onBack: () -> Unit) {
    PlaceholderScreen("Place Detail", "Selected place: ${state.placeId}\nDetails are planned for a later milestone.", onBack)
}