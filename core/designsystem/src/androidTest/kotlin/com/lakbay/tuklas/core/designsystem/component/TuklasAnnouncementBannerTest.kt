package com.lakbay.tuklas.core.designsystem.component

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class TuklasAnnouncementBannerTest {
    @get:Rule val compose = createComposeRule()

    @Test fun informationalBannerCombinesTextWithoutClickAction() {
        compose.setContent { TuklasTheme { TuklasAnnouncementBanner("Welcome", "Local favorites") } }
        compose.onNode(hasText("Welcome") and hasText("Local favorites")).assertIsDisplayed().assertHasNoClickAction()
    }

    @Test fun clickableBannerIsOneTargetAndForwardsAction() {
        var clicks = 0
        compose.setContent { TuklasTheme { TuklasAnnouncementBanner("Welcome", "Local favorites", onClick = { clicks++ }) } }
        compose.onAllNodes(hasClickAction()).assertCountEquals(1)
        compose.onNodeWithText("Welcome").assertHasClickAction().performClick()
        compose.runOnIdle { assertEquals(1, clicks) }
    }

    @Test fun titleOnlyAndLongContentAreSupportedInDarkTheme() {
        val title = "Long development announcement ".repeat(4)
        val description = "A long illustrative description ".repeat(6)
        compose.setContent { TuklasTheme(darkTheme = true) {
            TuklasAnnouncementBanner(title, description)
        } }
        compose.onNode(hasText(title) and hasText(description)).assertIsDisplayed()
    }

    @Test fun missingDescriptionDoesNotCreateAnotherAnnouncement() {
        compose.setContent { TuklasTheme { TuklasAnnouncementBanner("Title only", " ") } }
        compose.onNodeWithText("Title only").assertIsDisplayed().assertHasNoClickAction()
        compose.onNodeWithText(" ").assertDoesNotExist()
    }
}