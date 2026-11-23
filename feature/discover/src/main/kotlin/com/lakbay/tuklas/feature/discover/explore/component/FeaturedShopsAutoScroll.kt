package com.lakbay.tuklas.feature.discover.explore.component

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

/** The composition owns cancellation; every restart waits a full interval before advancing. */
internal suspend fun autoScrollFeaturedShops(
    intervalMillis: Long,
    shopCount: Int,
    currentIndex: () -> Int,
    advance: suspend (index: Int, wrapping: Boolean) -> Unit,
) {
    require(intervalMillis > 0) { "Auto-scroll interval must be positive" }
    if (shopCount <= 1) return
    while (currentCoroutineContext().isActive) {
        delay(intervalMillis)
        val current = currentIndex().coerceIn(0, shopCount - 1)
        val next = (current + 1) % shopCount
        advance(next, next == 0)
    }
}

internal fun restoredFeaturedShopIndex(ids: List<String>, selectedId: String?, fallbackIndex: Int): Int {
    if (ids.isEmpty()) return 0
    val match = ids.indexOf(selectedId)
    return if (match >= 0) match else fallbackIndex.coerceIn(ids.indices)
}