package com.lakbay.tuklas.feature.discover.explore

import org.junit.Assert.*
import org.junit.Test

class AnnouncementVisibilityTest {
    private val announcement = AnnouncementUiModel("welcome", "Welcome", "Local favorites")

    @Test fun productionDefaultsToNoAnnouncement() {
        assertNull(ExploreUiState.Content(emptyList()).visibleAnnouncement)
    }
    @Test fun activeAnnouncementIsVisibleWithEmptyOrWhitespaceQuery() {
        val state = ExploreUiState.Content(emptyList(), announcement = announcement)
        assertEquals(announcement, state.visibleAnnouncement)
        assertEquals(announcement, state.copy(searchQuery = "  ").visibleAnnouncement)
    }
    @Test fun searchHidesAndClearRestoresAvailableAnnouncement() {
        val state = ExploreUiState.Content(emptyList(), announcement = announcement)
        val searching = state.copy(searchQuery = " coffee ")
        assertNull(searching.visibleAnnouncement)
        assertEquals(announcement, searching.copy(searchQuery = "").visibleAnnouncement)
    }
    @Test fun inactiveAnnouncementIsHidden() {
        val state = ExploreUiState.Content(emptyList(), announcement = announcement.copy(isActive = false))
        assertNull(state.visibleAnnouncement)
    }
}