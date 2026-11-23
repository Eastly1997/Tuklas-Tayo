package com.lakbay.tuklas.feature.discover.presentation.search

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchRoute(onPlaceSelected: (String) -> Unit) {
    val viewModel: SearchViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var requestedInitialFocus by rememberSaveable { mutableStateOf(false) }
    SearchScreen(state, viewModel::onIntent, onPlaceSelected = { id ->
        keyboard?.hide()
        focusManager.clearFocus()
        onPlaceSelected(id)
    },
        searchFieldModifier = Modifier.focusRequester(focusRequester))
    LaunchedEffect(Unit) {
        if (!requestedInitialFocus) {
            requestedInitialFocus = true
            withFrameNanos { }
            focusRequester.requestFocus()
            keyboard?.show()
        }
    }
}