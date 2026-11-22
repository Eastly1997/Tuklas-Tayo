package com.lakbay.tuklas.feature.placedetail

import androidx.compose.runtime.Composable
import com.lakbay.tuklas.feature.placedetail.presentation.PlaceDetailScreen
import com.lakbay.tuklas.feature.placedetail.presentation.contract.PlaceDetailUiState

@Composable
fun PlaceDetailRoute(placeId: String, onBack: () -> Unit) {
    PlaceDetailScreen(state = PlaceDetailUiState(placeId), onBack = onBack)
}