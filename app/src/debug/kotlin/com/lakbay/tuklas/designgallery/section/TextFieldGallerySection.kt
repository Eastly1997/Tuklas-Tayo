package com.lakbay.tuklas.designgallery.section

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.lakbay.tuklas.designgallery.GallerySection
import com.lakbay.tuklas.core.designsystem.component.TuklasButton
import com.lakbay.tuklas.core.designsystem.component.TuklasButtonVariant
import com.lakbay.tuklas.core.designsystem.component.TuklasTextField
import com.lakbay.tuklas.core.designsystem.component.TuklasTextFieldVariant

@Composable
internal fun TextFieldGallerySection() = GallerySection("Text fields") {
    TuklasTextFieldVariant.entries.forEach { variant -> FieldExamples(variant) }
}

@Composable
private fun FieldExamples(variant: TuklasTextFieldVariant) {
    var value by rememberSaveable(variant) { mutableStateOf("") }
    var errorValue by rememberSaveable(variant) { mutableStateOf("Illustrative value") }
    val focusRequester = remember { FocusRequester() }
    Text(variant.name)
    TuklasTextField(
        value, { value = it }, Modifier.fillMaxWidth().focusRequester(focusRequester), variant,
        label = { Text("${variant.name} input") }, placeholder = { Text("Illustrative input") }, singleLine = true,
    )
    TuklasButton(onClick = { focusRequester.requestFocus() }, variant = TuklasButtonVariant.TEXT) {
        Text("Focus ${variant.name.lowercase()} input")
    }
    TuklasTextField(
        errorValue, { errorValue = it }, Modifier.fillMaxWidth(), variant,
        label = { Text("Error example") }, isError = true, singleLine = true,
        supportingText = { Text("Illustrative error message") },
    )
    TuklasTextField(
        "Disabled sample", {}, Modifier.fillMaxWidth(), variant,
        label = { Text("Disabled input") }, enabled = false,
    )
}
