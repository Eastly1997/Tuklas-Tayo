package com.lakbay.tuklas.feature.discover.shared

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme
import com.lakbay.tuklas.domain.model.Place
import com.lakbay.tuklas.domain.model.PlaceCategory
import com.lakbay.tuklas.feature.discover.shared.component.ExploreShopCard
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ExploreShopCardTest {
    @get:Rule val compose = createComposeRule()
    private val place = Place("sample", "Sample Café", PlaceCategory.CAFE,
        "123 Sample Street, Sample Barangay, Malolos", "Provided highlight", city = "Malolos")

    @Test fun sharedRowUsesStructuredLocationAndOneClickTarget() {
        var clicks = 0
        compose.setContent { TuklasTheme { ExploreShopCard(place, { clicks++ }) } }
        compose.onNodeWithText("Sample Café").assertIsDisplayed().assertHasClickAction()
        compose.onNodeWithText("Café · Malolos").assertIsDisplayed()
        compose.onNodeWithText("Provided highlight").assertIsDisplayed()
        compose.onNodeWithText(place.address).assertDoesNotExist()
        compose.onAllNodes(hasClickAction()).assertCountEquals(1)
        compose.onNodeWithText("Sample Café").performClick()
        compose.runOnIdle { assertEquals(1, clicks) }
    }

    @Test fun missingCityHighlightAndImageHaveNoInventedFallbackText() {
        compose.setContent { TuklasTheme(darkTheme = true) {
            ExploreShopCard(place.copy(city = null, description = "", imageUrl = null), {})
        } }
        compose.onNodeWithText("Sample Café").assertIsDisplayed()
        compose.onNodeWithText("Malolos").assertDoesNotExist()
        compose.onNodeWithText("Café").assertIsDisplayed()
        compose.onNodeWithText(place.address).assertDoesNotExist()
        compose.onNodeWithText("Provided highlight").assertDoesNotExist()
    }
}