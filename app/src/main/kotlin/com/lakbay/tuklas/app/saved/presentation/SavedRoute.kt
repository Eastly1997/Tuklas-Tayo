package com.lakbay.tuklas.app.saved.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.lakbay.tuklas.app.R
import com.lakbay.tuklas.app.saved.presentation.contract.SavedUiState

@Composable
internal fun SavedRoute() {
    SavedScreen(SavedUiState(
        title = stringResource(R.string.navigation_saved),
        message = stringResource(R.string.saved_placeholder),
    ))
}