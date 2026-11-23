package com.lakbay.tuklas.core.designsystem.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import com.lakbay.tuklas.core.designsystem.theme.TuklasSpacing
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme

@Preview(name = "Search Field Light", showBackground = true, widthDp = 400, heightDp = 360)
@Preview(name = "Search Field Dark", showBackground = true, widthDp = 400, heightDp = 360,
    uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Search Field Large text", showBackground = true, widthDp = 400, heightDp = 600, fontScale = 2f)
@Composable
private fun TuklasSearchFieldPreview() {
    TuklasTheme(dynamicColor = false) {
        Surface {
            val focusRequester = remember { FocusRequester() }
            var empty by remember { mutableStateOf("") }
            var query by remember { mutableStateOf("Coffee") }
            Column(Modifier.padding(TuklasSpacing.large), verticalArrangement = Arrangement.spacedBy(TuklasSpacing.large)) {
                TuklasSearchField(empty, { empty = it }, Modifier.fillMaxWidth())
                TuklasSearchField(query, { query = it }, Modifier.fillMaxWidth())
                TuklasSearchField("Disabled", {}, Modifier.fillMaxWidth(), enabled = false)
                TuklasSearchField("", {}, Modifier.fillMaxWidth().focusRequester(focusRequester),
                    placeholder = "Focused search")
            }
            LaunchedEffect(focusRequester) { focusRequester.requestFocus() }
        }
    }
}