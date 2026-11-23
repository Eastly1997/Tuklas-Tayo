package com.lakbay.tuklas.feature.saved

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.lakbay.tuklas.app.R

@Composable
internal fun SavedRoute() {
    SavedScreen(SavedUiState(
        title = stringResource(R.string.navigation_saved),
        message = stringResource(R.string.saved_placeholder),
    ))
}