package com.lakbay.tuklas.feature.discover.explore.component

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FeaturedShopsAutoScrollTest {
    @Test fun countdownRunsAfterSettlingAndWrapsWithoutIntermediateSteps() = runTest {
        var current = 0
        val visits = mutableListOf<Pair<Int, Boolean>>()
        val job = backgroundScope.launch {
            autoScrollFeaturedShops(6_000, 3, { current }) { index, wrapping ->
                visits += index to wrapping
                if (!wrapping) delay(450) // Animation must finish before the next countdown.
                current = index
            }
        }
        runCurrent()
        advanceTimeBy(5_999)
        assertTrue(visits.isEmpty())
        advanceTimeBy(1)
        runCurrent()
        assertEquals(listOf(1 to false), visits)
        advanceTimeBy(450 + 5_999)
        assertEquals(1, visits.size)
        advanceTimeBy(1)
        runCurrent()
        assertEquals(listOf(1 to false, 2 to false), visits)
        advanceTimeBy(450 + 6_000)
        runCurrent()
        assertEquals(listOf(1 to false, 2 to false, 0 to true), visits)
        job.cancel()
    }

    @Test fun cancellationAndRestartRequireAFullNewCountdown() = runTest {
        var advances = 0
        suspend fun runTimer() = autoScrollFeaturedShops(6_000, 2, { 0 }) { _, _ -> advances++ }
        val initial = backgroundScope.launch { runTimer() }
        runCurrent()
        advanceTimeBy(5_000)
        initial.cancel()
        runCurrent()
        advanceTimeBy(10_000)
        assertEquals(0, advances)
        val resumed = backgroundScope.launch { runTimer() }
        runCurrent()
        advanceTimeBy(5_999)
        assertEquals(0, advances)
        advanceTimeBy(1)
        runCurrent()
        assertEquals(1, advances)
        resumed.cancel()
    }

    @Test fun emptyAndSingleShopListsDoNotScheduleAdvancement() = runTest {
        for (count in 0..1) {
            autoScrollFeaturedShops(6_000, count, { 0 }) { _, _ -> fail("Must not advance") }
        }
        assertEquals(0L, testScheduler.currentTime)
    }

    @Test fun reorderedDataPreservesSelectedBusinessById() {
        assertEquals(0, restoredFeaturedShopIndex(listOf("second", "first", "third"), "second", 1))
    }

    @Test fun removedBusinessesAndShrinkingListsHaveValidFallbacks() {
        assertEquals(0, restoredFeaturedShopIndex(listOf("remaining"), "removed", 8))
        assertEquals(0, restoredFeaturedShopIndex(listOf("remaining"), null, -2))
        assertEquals(0, restoredFeaturedShopIndex(emptyList(), "removed", 8))
    }
}