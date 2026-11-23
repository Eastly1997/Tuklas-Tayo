package com.lakbay.tuklas.feature.discover.search

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme
import com.lakbay.tuklas.feature.discover.domain.model.Place
import com.lakbay.tuklas.feature.discover.domain.model.PlaceCategory
import com.lakbay.tuklas.feature.discover.search.SearchIntent
import com.lakbay.tuklas.feature.discover.search.SearchScreen
import com.lakbay.tuklas.feature.discover.search.SearchUiState
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class SearchScreenTest {
    @get:Rule val compose = createComposeRule()

    @Test fun initialPromptAndBottomFieldRenderWithoutRuntimeDependencies() {
        compose.setContent { TuklasTheme {
            SearchScreen(
                SearchUiState.Content("", emptyList()),
                {},
                {})
        } }
        val prompt = compose.onNodeWithText("What are you looking for?").assertIsDisplayed().fetchSemanticsNode().boundsInRoot
        val field = compose.onNode(hasSetTextAction()).assertIsDisplayed().fetchSemanticsNode().boundsInRoot
        assertTrue(field.top > prompt.bottom)
    }

    @Test fun resultClickAndClearAreForwardedWithoutErasingCallerState() {
        val place = Place("sample", "Sample Café", PlaceCategory.CAFE, "Sample City", "")
        val state = mutableStateOf<SearchUiState>(SearchUiState.Content("café", listOf(place)))
        val intents = mutableListOf<SearchIntent>()
        var selected: String? = null
        compose.setContent { TuklasTheme {
            SearchScreen(
                state.value,
                { intents += it },
                { selected = it })
        } }
        compose.onNodeWithText("Sample Café").performClick()
        compose.runOnIdle { assertEquals("sample", selected) }
        compose.onNodeWithContentDescription("Clear search").performClick()
        compose.runOnIdle {
            assertEquals(listOf(SearchIntent.QueryChanged("")), intents)
            state.value = SearchUiState.Content("", emptyList())
        }
        compose.onNodeWithText("What are you looking for?").assertIsDisplayed()
    }

    @Test fun noMatchAndErrorStatesRemainSearchable() {
        val state = mutableStateOf<SearchUiState>(SearchUiState.Content("missing", emptyList()))
        val intents = mutableListOf<SearchIntent>()
        compose.setContent { TuklasTheme { SearchScreen(state.value, { intents += it }, {}) } }
        compose.onNodeWithText("No shops found").assertIsDisplayed()
        compose.onNode(hasSetTextAction()).assertIsEnabled()
        compose.runOnIdle { state.value = SearchUiState.Error("missing") }
        compose.onNodeWithText("Retry").performClick()
        compose.runOnIdle { assertEquals(listOf(SearchIntent.Retry), intents) }
    }
}