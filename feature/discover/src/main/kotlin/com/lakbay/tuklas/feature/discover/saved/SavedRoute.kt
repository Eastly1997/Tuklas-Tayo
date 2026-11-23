package com.lakbay.tuklas.feature.discover.saved

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.lakbay.tuklas.feature.discover.R

@Composable
internal fun SavedRoute() {
    SavedScreen(SavedUiState(
        title = stringResource(R.string.navigation_saved),
        message = stringResource(R.string.saved_placeholder),
    ))
}