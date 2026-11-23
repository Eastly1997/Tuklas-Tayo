package com.lakbay.tuklas.feature.discover.explore

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme
import com.lakbay.tuklas.domain.model.Place
import com.lakbay.tuklas.domain.model.PlaceCategory
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class ExploreAnnouncementTest {
    @get:Rule val compose = createComposeRule()

    @Test fun bannerAppearsBetweenSearchAndFeaturedAndFollowsSearchVisibility() {
        val announcement = AnnouncementUiModel("welcome", "Welcome announcement", "Local favorites",
            destination = AnnouncementDestination.PlaceDetail("sample"))
        val normal = ExploreUiState.Content(
            listOf(Place("sample", "Sample", PlaceCategory.CAFE, "Sample address", "")),
            announcement = announcement,
        )
        val state = mutableStateOf(normal)
        var selected: AnnouncementDestination? = null
        compose.setContent { TuklasTheme {
            ExploreScreen(state.value, {}, {}, onAnnouncementSelected = { selected = it })
        } }
        val banner = compose.onNodeWithText("Welcome announcement")
        val search = compose.onNodeWithText("Search shops or places").fetchSemanticsNode().boundsInRoot
        val featured = compose.onNodeWithText("Featured Shops").fetchSemanticsNode().boundsInRoot
        val bounds = banner.fetchSemanticsNode().boundsInRoot
        assertTrue(bounds.top >= search.bottom && bounds.bottom <= featured.top)
        banner.performClick()
        compose.runOnIdle {
            assertEquals(AnnouncementDestination.PlaceDetail("sample"), selected)
            state.value = normal.copy(searchQuery = "coffee", featuredShops = emptyList())
        }
        compose.onNodeWithText("Welcome announcement").assertDoesNotExist()
        compose.runOnIdle { state.value = normal }
        compose.onNodeWithText("Welcome announcement").assertIsDisplayed()
        compose.runOnIdle { state.value = normal.copy(announcement = null) }
        compose.onNodeWithText("Welcome announcement").assertDoesNotExist()
    }

    @Test fun missingDestinationHandlerDoesNotExposeADeadAction() {
        val announcement = AnnouncementUiModel("welcome", "Informational announcement",
            destination = AnnouncementDestination.PlaceDetail("sample"))
        compose.setContent { TuklasTheme {
            ExploreScreen(ExploreUiState.Content(emptyList(), announcement = announcement), {}, {})
        } }
        compose.onNodeWithText("Informational announcement").assertIsDisplayed().assertHasNoClickAction()
    }
}