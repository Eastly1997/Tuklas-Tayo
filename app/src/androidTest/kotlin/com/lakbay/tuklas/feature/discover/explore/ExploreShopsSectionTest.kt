package com.lakbay.tuklas.feature.discover.explore

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme
import com.lakbay.tuklas.domain.model.Place
import com.lakbay.tuklas.domain.model.PlaceCategory
import com.lakbay.tuklas.feature.discover.explore.component.exploreShopsSection
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ExploreShopsSectionTest {
    @get:Rule val compose = createComposeRule()
    private val cafe = Place("cafe", "Sample Coffee", PlaceCategory.CAFE, "Sample City", "", city = "Sample City")

    @Test fun headingActualCountAndCategorySelectionUseProductionComponents() {
        var selected: PlaceCategory? = null
        compose.setContent { TuklasTheme {
            LazyColumn {
                exploreShopsSection(listOf(cafe), null, { selected = it }, {})
            }
        } }
        compose.onNodeWithText("Explore Shops").assertIsDisplayed()
        compose.onNodeWithText("1 place").assertIsDisplayed()
        compose.onNodeWithText("All").assertIsSelected()
        compose.onNodeWithText("Café").performClick()
        compose.runOnIdle { assertEquals(PlaceCategory.CAFE, selected) }
    }

    @Test fun rowsRemainLazyAndCanReachTheEndWithoutNestedLists() {
        val places = List(200) { cafe.copy(id = "sample_$it", name = "Sample shop $it") }
        compose.setContent { TuklasTheme {
            LazyColumn(Modifier.testTag("grouped_list")) {
                exploreShopsSection(places, null, {}, {})
            }
        } }
        compose.onNodeWithText("Sample shop 199").assertDoesNotExist()
        compose.onNodeWithTag("grouped_list").performScrollToIndex(200)
        compose.onNodeWithText("Sample shop 199").assertIsDisplayed()
    }

    @Test fun emptyFilteredGroupRetainsHeaderAndControls() {
        compose.setContent { TuklasTheme {
            LazyColumn { exploreShopsSection(emptyList(), PlaceCategory.CAFE, {}, {}) }
        } }
        compose.onNodeWithText("Explore Shops").assertIsDisplayed()
        compose.onNodeWithText("0 places").assertIsDisplayed()
        compose.onNodeWithText("Café").assertIsSelected()
        compose.onNodeWithText("No shops found.").assertIsDisplayed()
    }
}