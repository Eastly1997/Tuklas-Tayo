package com.lakbay.tuklas.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SharedComponentsTest {
    @get:Rule val compose = createComposeRule()

    @Test fun buttonsExposeEnabledDisabledStatesAndForwardClicks() {
        val clicks = mutableMapOf<TuklasButtonVariant, Int>()
        compose.setContent {
            TuklasTheme {
                Column {
                    TuklasButtonVariant.entries.forEach { variant ->
                        TuklasButton(onClick = { clicks[variant] = (clicks[variant] ?: 0) + 1 }, variant = variant) {
                            Text(variant.name)
                        }
                        TuklasButton(onClick = {}, variant = variant, enabled = false) { Text("${variant.name} disabled") }
                    }
                }
            }
        }
        TuklasButtonVariant.entries.forEach { variant ->
            compose.onNodeWithText(variant.name).assertIsEnabled().performClick()
            compose.onNodeWithText("${variant.name} disabled").assertIsNotEnabled()
        }
        compose.runOnIdle { TuklasButtonVariant.entries.forEach { assertEquals(1, clicks[it]) } }
    }

    @Test fun cardsSeparateInteractiveAndStaticContainers() {
        var clicks = 0
        compose.setContent {
            TuklasTheme {
                Column {
                    TuklasCardVariant.entries.forEach { variant ->
                        TuklasCard(variant = variant, onClick = { clicks++ }) { Text(variant.name) }
                    }
                    TuklasCard { Text("Static") }
                    TuklasCard(onClick = {}, enabled = false) { Text("Disabled") }
                }
            }
        }
        TuklasCardVariant.entries.forEach { compose.onNodeWithText(it.name).performClick() }
        compose.onNodeWithText("Static").assertHasNoClickAction()
        compose.onNodeWithText("Disabled").assertIsNotEnabled()
        compose.runOnIdle { assertEquals(3, clicks) }
    }

    @Test fun chipSelectionIsHoistedAndOnlyPresentForSelectableTypes() {
        compose.setContent {
            var filter by remember { mutableStateOf(false) }
            var input by remember { mutableStateOf(false) }
            TuklasTheme {
                Column {
                    TuklasChip(onClick = { filter = !filter }, label = { Text("Filter") }, type = TuklasChipType.Filter(filter))
                    TuklasChip(onClick = { input = !input }, label = { Text("Input") }, type = TuklasChipType.Input(input))
                    TuklasChip(onClick = {}, label = { Text("Assist") })
                    TuklasChip(onClick = {}, label = { Text("Suggestion") }, type = TuklasChipType.Suggestion)
                }
            }
        }
        listOf("Filter", "Input").forEach { label ->
            compose.onNodeWithText(label).assertIsNotSelected().performClick().assertIsSelected()
        }
        listOf("Assist", "Suggestion").forEach { label ->
            compose.onNodeWithText(label).assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.Selected))
        }
    }

    @Test fun bothFieldVariantsForwardEditsAndExposeErrorAndDisabledStates() {
        compose.setContent {
            TuklasTheme {
                Column {
                    TuklasTextFieldVariant.entries.forEach { variant ->
                        var value by remember { mutableStateOf("") }
                        TuklasTextField(value, { value = it }, Modifier.testTag(variant.name), variant,
                            label = { Text(variant.name) })
                    }
                    TuklasTextField("Error", {}, Modifier.testTag("error"), isError = true,
                        label = { Text("Error sample") }, supportingText = { Text("Illustrative error") })
                    TuklasTextField("Disabled", {}, Modifier.testTag("disabled"), enabled = false,
                        label = { Text("Disabled sample") })
                }
            }
        }
        TuklasTextFieldVariant.entries.forEach { variant ->
            val field = compose.onNodeWithTag(variant.name)
            field.performTextInput("Illustrative input")
            field.assertTextContains("Illustrative input")
        }
        compose.onNodeWithTag("error").assert(SemanticsMatcher.keyIsDefined(SemanticsProperties.Error))
        compose.onNodeWithTag("disabled").assertIsNotEnabled()
    }
}
