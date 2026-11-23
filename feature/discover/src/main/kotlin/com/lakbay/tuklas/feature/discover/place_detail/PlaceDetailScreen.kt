package com.lakbay.tuklas.feature.discover.place_detail

import com.lakbay.tuklas.feature.discover.R

import androidx.compose.ui.res.stringResource

import androidx.compose.runtime.Composable
import com.lakbay.tuklas.core.designsystem.PlaceholderScreen

@Composable
internal fun PlaceDetailScreen(state: PlaceDetailUiState, onBack: () -> Unit) {
    PlaceholderScreen(stringResource(R.string.place_detail_title), stringResource(R.string.place_detail_placeholder, state.placeId), onBack)
}