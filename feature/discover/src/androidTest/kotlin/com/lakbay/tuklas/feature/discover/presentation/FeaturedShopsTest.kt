package com.lakbay.tuklas.feature.discover.presentation

import com.lakbay.tuklas.feature.discover.presentation.component.FeaturedShopsSection

import com.lakbay.tuklas.feature.discover.presentation.component.FeaturedShopCard

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme
import com.lakbay.tuklas.feature.discover.presentation.model.FeaturedShopUiModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class FeaturedShopsTest {
    @get:Rule val compose = createComposeRule()
    private val first = FeaturedShopUiModel("first", "Sample Café", null, "Café", "Malolos", null)

    @Test fun emptyCollectionHidesHeading() {
        compose.setContent { TuklasTheme { FeaturedShopsSection(emptyList(), {}) } }
        compose.onNodeWithText("Featured Shops").assertDoesNotExist()
    }

    @Test fun singleMissingImageCardIsAccessibleAndOpensItsId() {
        var selected: String? = null
        compose.setContent { TuklasTheme { FeaturedShopsSection(listOf(first), { selected = it }) } }
        compose.onNodeWithContentDescription("Café · Malolos. Sample Café")
            .assertIsDisplayed().assertHasClickAction().performClick()
        compose.runOnIdle { assertEquals("first", selected) }
        compose.onNodeWithTag("featured_carousel").assertDoesNotExist()
    }

    @Test fun carouselCanReachAndSelectAnotherShop() {
        var selected: String? = null
        val second = first.copy(id = "second", name = "Sample Second")
        compose.setContent { TuklasTheme { FeaturedShopsSection(listOf(first, second), { selected = it }) } }
        compose.onNodeWithTag("featured_carousel").performScrollToIndex(1)
        compose.onNodeWithContentDescription("Café · Malolos. Sample Second")
            .assertIsDisplayed().performClick()
        compose.runOnIdle { assertEquals("second", selected) }
    }

    @Test fun missingCityDoesNotLeaveAnEmptyMetadataSeparator() {
        compose.setContent { TuklasTheme {
            FeaturedShopCard(first.copy(city = null), {})
        } }
        compose.onNodeWithContentDescription("Café. Sample Café").assertIsDisplayed().assertHasClickAction()
    }

    @Test fun longNamesAndHighlightsRetainAccessibleInformation() {
        val name = "Sample shop with a deliberately very long name ".repeat(3)
        val highlight = "A long illustrative highlight ".repeat(5)
        compose.setContent { TuklasTheme(darkTheme = true) {
            FeaturedShopCard(first.copy(name = name, highlight = highlight), {})
        } }
        compose.onNodeWithContentDescription("Café · Malolos. $name. $highlight")
            .assertIsDisplayed().assertHasClickAction()
    }
}