package com.lakbay.tuklas.app

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

    @Test fun mockLaunchExploreDetailAndBack() {
        compose.onNodeWithTag("tab_discover").assertIsSelected()
        compose.onNodeWithText("Sample Kainan").assertIsDisplayed()
        compose.onNodeWithText("Explore").performClick()
        compose.onNodeWithText("Explore is planned for a later milestone.").assertIsDisplayed()
        compose.onNodeWithTag("tab_discover").assertDoesNotExist()
        compose.onNodeWithText("Back").performClick()
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
