package com.lakbay.tuklas.feature.discover.explore.component

import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.MotionDurationScale
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.findRootCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import com.lakbay.tuklas.core.designsystem.theme.TuklasSpacing
import com.lakbay.tuklas.feature.discover.shared.model.FeaturedShopUiModel
import kotlinx.coroutines.flow.filterNotNull
import kotlin.math.abs

@Composable
fun FeaturedShopsCarousel(
    shops: List<FeaturedShopUiModel>,
    onShopClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    autoScrollEnabled: Boolean = true,
    autoScrollIntervalMillis: Long = 6_000L,
    state: FeaturedShopsCarouselState = rememberFeaturedShopsCarouselState(),
) {
    if (shops.isEmpty()) return
    require(autoScrollIntervalMillis > 0) { "Auto-scroll interval must be positive" }
    val list = state.listState
    val ids = shops.map { it.id }
    val lifecycleState by LocalLifecycleOwner.current.lifecycle.currentStateAsState()
    val dragged by list.interactionSource.collectIsDraggedAsState()
    val motionScale = rememberCoroutineScope().coroutineContext[MotionDurationScale]?.scaleFactor ?: 1f
    val inspectionMode = LocalInspectionMode.current
    var visible by remember { mutableStateOf(false) }
    var pressed by remember { mutableStateOf(false) }
    var autoAnimating by remember { mutableStateOf(false) }
    var realigning by remember { mutableStateOf(true) }
    val spacingPx = with(LocalDensity.current) { TuklasSpacing.medium.toPx() }

    // ID restoration also handles insertion/reordering, removal and list shrinkage.
    LaunchedEffect(ids, list) {
        realigning = true
        try {
            val fallback = list.centeredItem()?.index ?: list.firstVisibleItemIndex
            val target = restoredFeaturedShopIndex(ids, state.selectedShopId, fallback)
            list.scrollToItem(target)
            state.selectedShopId = ids[target]
        } finally {
            realigning = false
        }
    }
    LaunchedEffect(list, state) {
        snapshotFlow {
            if (!realigning && !list.isScrollInProgress) list.centeredItem()?.key as? String else null
        }.filterNotNull().collect { state.selectedShopId = it }
    }

    val manuallyScrolling = list.isScrollInProgress && !autoAnimating
    val active = autoScrollEnabled && shops.size > 1 && !state.isPaused && visible &&
        lifecycleState == Lifecycle.State.RESUMED && !inspectionMode && motionScale > 0f &&
        !pressed && !dragged && !manuallyScrolling && !realigning
    LaunchedEffect(active, autoScrollIntervalMillis, ids, state.selectedShopId) {
        if (!active) return@LaunchedEffect
        autoScrollFeaturedShops(autoScrollIntervalMillis, shops.size,
            currentIndex = { list.centeredItem()?.index ?: list.firstVisibleItemIndex },
        ) { _, wrapping ->
            autoAnimating = true
            try {
                if (wrapping) {
                    // A direct reset avoids a distracting traverse through intermediate cards.
                    list.scrollToItem(0)
                } else {
                    val item = list.centeredItem() ?: return@autoScrollFeaturedShops
                    val info = list.layoutInfo
                    val center = (info.viewportStartOffset + info.viewportEndOffset) / 2f
                    val distance = item.offset + item.size / 2f - center + item.size + spacingPx
                    list.animateScrollBy(distance, animationSpec = tween(durationMillis = 450))
                }
            } finally {
                autoAnimating = false
            }
        }
    }

    BoxWithConstraints(modifier.fillMaxWidth()) {
        val cardWidth = (maxWidth - 48.dp).coerceAtLeast(1.dp).coerceAtMost(600.dp)
        val sidePadding = (maxWidth - cardWidth) / 2
        LazyRow(
            state = list,
            userScrollEnabled = shops.size > 1,
            flingBehavior = rememberSnapFlingBehavior(list, snapPosition = SnapPosition.Center),
            contentPadding = PaddingValues(horizontal = sidePadding),
            horizontalArrangement = Arrangement.spacedBy(TuklasSpacing.medium),
            modifier = Modifier.fillMaxWidth().testTag("featured_carousel")
                .onGloballyPositioned { coordinates ->
                    val bounds = coordinates.boundsInWindow()
                    val root = coordinates.findRootCoordinates().size
                    visible = bounds.width > 0 && bounds.height > 0 &&
                        bounds.bottom > 0 && bounds.top < root.height &&
                        bounds.right > 0 && bounds.left < root.width
                }
                // Observe touches before children without consuming them or replacing card clicks.
                .pointerInput(Unit) {
                    try {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent(PointerEventPass.Initial)
                                pressed = event.changes.any { it.pressed }
                            }
                        }
                    } finally {
                        pressed = false
                    }
                },
        ) {
            items(shops, key = { it.id }) { shop ->
                FeaturedShopCard(shop, { onShopClick(shop.id) }, Modifier.width(cardWidth))
            }
        }
    }
}

private fun LazyListState.centeredItem(): LazyListItemInfo? {
    val info = layoutInfo
    val center = (info.viewportStartOffset + info.viewportEndOffset) / 2f
    return info.visibleItemsInfo.minByOrNull { abs(it.offset + it.size / 2f - center) }
}