package com.lakbay.tuklas.core.designsystem.component

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class TuklasSearchTriggerTest {
    @get:Rule val compose = createComposeRule()

    @Test fun triggerIsClickableWithoutAnEditor() {
        var calls = 0
        compose.setContent { TuklasTheme { TuklasSearchTrigger({ calls++ }) } }
        compose.onAllNodes(hasSetTextAction()).assertCountEquals(0)
        compose.onNodeWithText("Search shops or places").assertHasClickAction()
            .assertHeightIsAtLeast(56.dp).performClick()
        compose.runOnIdle { assertEquals(1, calls) }
    }
}