package com.lakbay.tuklas.feature.discover.explore.component

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/** UI-only state, hoisted above conditional/lazy content to preserve position and user pause. */
@Stable
class FeaturedShopsCarouselState internal constructor(
    val listState: LazyListState,
    selectedShop: MutableState<String?>,
    paused: MutableState<Boolean>,
) {
    internal var selectedShopId by selectedShop
    var isPaused by paused
}

@Composable
fun rememberFeaturedShopsCarouselState(): FeaturedShopsCarouselState {
    val listState = rememberLazyListState()
    val selectedShop = rememberSaveable { mutableStateOf<String?>(null) }
    val paused = rememberSaveable { mutableStateOf(false) }
    return remember(listState, selectedShop, paused) {
        FeaturedShopsCarouselState(listState, selectedShop, paused)
    }
}