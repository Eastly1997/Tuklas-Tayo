package com.lakbay.tuklas.core.designsystem.component

import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class TuklasSearchFieldTest {
    @get:Rule val compose = createComposeRule()

    @Test fun typingAndClearingUsesCallerStateAndRetainsFocus() {
        compose.setContent {
            var query by remember { mutableStateOf("") }
            TuklasTheme { TuklasSearchField(query, { query = it }) }
        }
        val field = compose.onNode(hasSetTextAction())
        compose.onNodeWithContentDescription("Clear search").assertDoesNotExist()
        field.performTextInput("Coffee")
        field.assertTextContains("Coffee").assertIsFocused()
        compose.onNodeWithContentDescription("Clear search").performClick()
        field.assertTextContains("").assertIsFocused()
        compose.onNodeWithContentDescription("Clear search").assertDoesNotExist()
    }

    @Test fun disabledSearchAndClearExposeDisabledSemantics() {
        compose.setContent { TuklasTheme { TuklasSearchField("Coffee", {}, enabled = false) } }
        compose.onNode(hasSetTextAction()).assertIsNotEnabled()
        compose.onNodeWithContentDescription("Clear search").assertIsNotEnabled()
    }

    @Test fun customClearCallbackIsUsed() {
        var calls = 0
        compose.setContent { TuklasTheme {
            TuklasSearchField("Coffee", { error("Custom clear should handle the action") }, onClear = { calls++ })
        } }
        compose.onNodeWithContentDescription("Clear search").performClick()
        compose.runOnIdle { assertEquals(1, calls) }
    }

    @Test fun inputDoesNotOwnQueryOrNavigateOnSearchAction() {
        val changes = mutableListOf<String>()
        compose.setContent { TuklasTheme { TuklasSearchField("Coffee", { changes += it }) } }
        val field = compose.onNode(hasSetTextAction())
        field.performImeAction()
        field.assertTextContains("Coffee")
        compose.runOnIdle { assertEquals(emptyList<String>(), changes) }
    }
}