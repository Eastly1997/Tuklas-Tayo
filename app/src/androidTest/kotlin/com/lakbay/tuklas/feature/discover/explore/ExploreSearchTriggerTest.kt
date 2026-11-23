package com.lakbay.tuklas.feature.discover.explore

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme
import com.lakbay.tuklas.domain.model.Place
import com.lakbay.tuklas.domain.model.PlaceCategory
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ExploreSearchTriggerTest {
    @get:Rule val compose = createComposeRule()

    @Test fun triggerRequestsNavigationWithoutEditingDiscoverState() {
        val state = ExploreUiState.Content(
            listOf(Place("sample", "Sample Café", PlaceCategory.CAFE, "Sample address", "Coffee")),
            announcement = AnnouncementUiModel("welcome", "Welcome announcement"),
        )
        var searches = 0
        compose.setContent { TuklasTheme {
            ExploreScreen(state, { error("Trigger must not dispatch an inline search intent") }, {},
                onSearch = { searches++ })
        } }
        compose.onAllNodes(hasSetTextAction()).assertCountEquals(0)
        compose.onNodeWithText("Search shops or places").performClick()
        compose.runOnIdle { assertEquals(1, searches) }
        compose.onNodeWithText("Welcome announcement").assertIsDisplayed()
        compose.onNodeWithText("Featured Shops").assertIsDisplayed()
    }
}