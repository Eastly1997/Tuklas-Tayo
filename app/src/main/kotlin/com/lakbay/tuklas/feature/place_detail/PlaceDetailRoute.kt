package com.lakbay.tuklas.feature.place_detail

import androidx.compose.runtime.Composable

@Composable
fun PlaceDetailRoute(placeId: String, onBack: () -> Unit) {
    PlaceDetailScreen(state = PlaceDetailUiState(placeId), onBack = onBack)
}