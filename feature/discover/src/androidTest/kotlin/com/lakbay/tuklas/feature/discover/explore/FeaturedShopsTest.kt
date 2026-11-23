package com.lakbay.tuklas.feature.discover.explore

import com.lakbay.tuklas.feature.discover.explore.component.FeaturedShopsSection

import com.lakbay.tuklas.feature.discover.explore.component.FeaturedShopCard

import androidx.compose.ui.test.*
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.runtime.mutableStateOf
import org.junit.Assert.assertTrue
import androidx.compose.ui.test.junit4.createComposeRule
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme
import com.lakbay.tuklas.feature.discover.shared.model.FeaturedShopUiModel
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
        compose.onNodeWithTag("featured_auto_scroll_toggle").assertDoesNotExist()
    }

    @Test fun carouselCanReachAndSelectAnotherShop() {
        var selected: String? = null
        val second = first.copy(id = "second", name = "Sample Second")
        compose.setContent { TuklasTheme { FeaturedShopsSection(listOf(first, second), { selected = it }, autoScrollEnabled = false) } }
        compose.onNodeWithTag("featured_carousel").performScrollToIndex(1)
        compose.onNodeWithContentDescription("Café · Malolos. Sample Second")
            .assertIsDisplayed().performClick()
        compose.runOnIdle { assertEquals("second", selected) }
    }

    @Test fun firstAndLastCardsAreCentered() {
        val second = first.copy(id = "second", name = "Sample Second")
        val third = first.copy(id = "third", name = "Sample Third")
        compose.setContent { TuklasTheme {
            FeaturedShopsSection(listOf(first, second, third), {}, autoScrollEnabled = false)
        } }
        fun assertCentered(description: String) {
            val viewport = compose.onNodeWithTag("featured_carousel").getUnclippedBoundsInRoot()
            val card = compose.onNodeWithContentDescription(description).getUnclippedBoundsInRoot()
            assertTrue(kotlin.math.abs((viewport.left.value + viewport.right.value - card.left.value - card.right.value) / 2) < 1f)
        }
        assertCentered("Café · Malolos. Sample Café")
        val viewport = compose.onNodeWithTag("featured_carousel").fetchSemanticsNode().boundsInRoot
        val neighbor = compose.onNodeWithContentDescription("Café · Malolos. Sample Second").fetchSemanticsNode().boundsInRoot
        assertTrue(neighbor.width > 0f && neighbor.width < viewport.width)
        compose.onNodeWithTag("featured_carousel").performScrollToIndex(2)
        assertCentered("Café · Malolos. Sample Third")
    }

    @Test fun headingOffersPauseResumeActionsWithoutAVisibleButton() {
        compose.setContent { TuklasTheme {
            FeaturedShopsSection(listOf(first, first.copy(id = "second", name = "Sample Second")), {})
        } }
        compose.onNodeWithTag("featured_auto_scroll_toggle").assertDoesNotExist()
        val heading = compose.onNodeWithText("Featured Shops")
        heading.assertHasNoClickAction()
        val pause = heading.fetchSemanticsNode().config[SemanticsActions.CustomActions].single()
        assertEquals("Pause automatic featured shops", pause.label)
        compose.runOnIdle { assertTrue(pause.action()) }
        heading.assert(SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "Automatic advancement paused"))
        val resume = heading.fetchSemanticsNode().config[SemanticsActions.CustomActions].single()
        assertEquals("Resume automatic featured shops", resume.label)
        compose.runOnIdle { assertTrue(resume.action()) }
        heading.assert(SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "Automatic advancement enabled"))
    }
    @Test fun changingDataPreservesTheSelectedShop() {
        val second = first.copy(id = "second", name = "Sample Second")
        val data = mutableStateOf(listOf(first, second))
        compose.setContent { TuklasTheme {
            FeaturedShopsSection(data.value, {}, autoScrollEnabled = false)
        } }
        compose.onNodeWithTag("featured_carousel").performScrollToIndex(1)
        compose.runOnIdle { data.value = listOf(second, first) }
        val viewport = compose.onNodeWithTag("featured_carousel").getUnclippedBoundsInRoot()
        val selected = compose.onNodeWithContentDescription("Café · Malolos. Sample Second").getUnclippedBoundsInRoot()
        assertTrue(kotlin.math.abs((viewport.left.value + viewport.right.value - selected.left.value - selected.right.value) / 2) < 1f)
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