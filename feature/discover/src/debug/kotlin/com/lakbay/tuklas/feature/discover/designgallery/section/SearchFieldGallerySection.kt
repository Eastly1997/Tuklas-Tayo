package com.lakbay.tuklas.feature.discover.designgallery.section

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
import com.lakbay.tuklas.feature.discover.designgallery.GallerySection
import com.lakbay.tuklas.core.designsystem.component.TuklasButton
import com.lakbay.tuklas.core.designsystem.component.TuklasSearchField
import com.lakbay.tuklas.core.designsystem.component.TuklasSearchTrigger
import androidx.compose.foundation.layout.fillMaxWidth

@Composable
internal fun SearchFieldGallerySection(onAction: (String) -> Unit) {
    var emptyQuery by rememberSaveable { mutableStateOf("") }
    var enteredQuery by rememberSaveable { mutableStateOf("Coffee") }
    var focusQuery by rememberSaveable { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    GallerySection("Search field") {
        Text("Navigation trigger • no editor or keyboard")
        TuklasSearchTrigger({ onAction("Search trigger selected") }, Modifier.fillMaxWidth())
        Text("Default • type to reveal Clear search")
        TuklasSearchField(emptyQuery, { emptyQuery = it }, Modifier.fillMaxWidth())
        Text("Entered text • clear retains focus")
        TuklasSearchField(enteredQuery, { enteredQuery = it }, Modifier.fillMaxWidth())
        Text("Focused example")
        TuklasSearchField(focusQuery, { focusQuery = it },
            Modifier.fillMaxWidth().focusRequester(focusRequester))
        TuklasButton(onClick = { focusRequester.requestFocus() }) { Text("Focus example") }
        Text("Disabled")
        TuklasSearchField("Coffee", {}, Modifier.fillMaxWidth(), enabled = false)
    }
}