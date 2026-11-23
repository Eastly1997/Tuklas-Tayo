package com.lakbay.tuklas

import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppNavigationTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test fun dedicatedSearchNavigatesToDetailsAndRestoresQuery() {
        compose.onNodeWithText("Search shops or places").performClick()
        compose.onNodeWithText("Search Places").assertIsDisplayed()
        compose.onNodeWithTag("tab_discover").assertDoesNotExist()
        compose.onNodeWithText("What are you looking for?").assertIsDisplayed()
        compose.onNode(hasSetTextAction()).performTextInput("Sample Kape")
        compose.onNodeWithText("Sample Kape").performClick()
        compose.onNodeWithText("Selected place: sample-kape", substring = true).assertIsDisplayed()
        compose.onNodeWithText("Back").performClick()
        compose.onNode(hasSetTextAction()).assertTextContains("Sample Kape")
        compose.onNodeWithText("Sample Kape").assertIsDisplayed()
        compose.onNodeWithContentDescription("Back").performClick()
        compose.onNodeWithTag("tab_discover").assertIsSelected()
    }
    @Test fun mockLaunchDetailAndBack() {
        compose.onNodeWithTag("tab_discover").assertIsSelected()
        compose.onNodeWithText("Sample Kainan").assertIsDisplayed()
        compose.onNodeWithText("Explore").assertDoesNotExist()
        compose.onNodeWithText("Sample Kainan").performClick()
        compose.onNodeWithText("Place Detail").assertIsDisplayed()
        compose.onNodeWithTag("tab_discover").assertDoesNotExist()
        compose.onNodeWithText("Selected place: sample-kainan", substring = true).assertIsDisplayed()
        compose.onNodeWithText("Back").performClick()
        compose.onNodeWithTag("tab_discover").assertIsSelected()
    }

    @Test fun topLevelTabsRestoreGalleryState() {
        compose.onNodeWithTag("tab_saved").performClick()
        compose.onNodeWithText("Saved places will appear here in a future milestone.").assertIsDisplayed()
        compose.onNodeWithTag("tab_debug").performClick()
        compose.onNodeWithText("TuklasTayo UI Kit").assertIsDisplayed()
        compose.onNodeWithText("Dark").performClick().assertIsSelected()
        compose.onNodeWithTag("tab_discover").performClick()
        compose.onNodeWithText("Sample Kainan").assertIsDisplayed()
        compose.onNodeWithTag("tab_debug").performClick()
        compose.onNodeWithText("Dark").assertIsSelected()
    }
}
