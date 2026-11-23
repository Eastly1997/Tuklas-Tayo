package com.lakbay.tuklas.core.designsystem.component

import androidx.compose.material3.Text
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme
import org.junit.Rule
import org.junit.Test

class TuklasSectionContainerTest {
    @get:Rule val compose = createComposeRule()

    @Test fun arbitraryContentRendersWithoutMakingTheGroupClickable() {
        compose.setContent { TuklasTheme {
            TuklasSectionContainer {
                Text("Heading")
                Text("Body")
                TuklasButton({}) { Text("Action") }
            }
        } }
        compose.onNodeWithText("Heading").assertIsDisplayed().assertHasNoClickAction()
        compose.onNodeWithText("Body").assertIsDisplayed()
        compose.onAllNodes(hasClickAction()).assertCountEquals(1)
    }

    @Test fun defaultContainerSupportsDarkTheme() {
        compose.setContent { TuklasTheme(darkTheme = true) { TuklasSectionContainer { Text("Dark content") } } }
        compose.onNodeWithText("Dark content").assertIsDisplayed()
    }
}