package com.lakbay.tuklas.core.designsystem.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lakbay.tuklas.core.designsystem.theme.TuklasSpacing
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme

@Composable
private fun PreviewContent(content: @Composable () -> Unit) {
    TuklasTheme {
        Surface {
            Column(Modifier.padding(TuklasSpacing.large),
                verticalArrangement = Arrangement.spacedBy(TuklasSpacing.small)) { content() }
        }
    }
}

@Preview(name = "Buttons Light", showBackground = true, widthDp = 360)
@Preview(name = "Buttons Dark", showBackground = true, widthDp = 360, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ButtonPreviews() = PreviewContent {
    TuklasButtonVariant.entries.forEach { variant ->
        TuklasButton(onClick = {}, variant = variant) { Text(variant.name) }
        TuklasButton(onClick = {}, variant = variant, enabled = false) { Text("Disabled ${variant.name}") }
    }
}

@Preview(name = "Cards Light", showBackground = true, widthDp = 360)
@Preview(name = "Cards Dark", showBackground = true, widthDp = 360, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CardPreviews() = PreviewContent {
    TuklasCardVariant.entries.forEach { variant ->
        TuklasCard(Modifier.fillMaxWidth(), variant, onClick = {}) {
            Text("Illustrative ${variant.name} card", Modifier.padding(TuklasSpacing.large))
        }
    }
    TuklasCard(Modifier.fillMaxWidth()) {
        Text("Static card", Modifier.padding(TuklasSpacing.large))
    }
    TuklasCard(Modifier.fillMaxWidth(), onClick = {}, enabled = false) {
        Text("Disabled interactive card", Modifier.padding(TuklasSpacing.large))
    }
}

@Preview(name = "Chips Light", showBackground = true, widthDp = 360)
@Preview(name = "Chips Dark", showBackground = true, widthDp = 360, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ChipPreviews() = PreviewContent {
    listOf(TuklasChipType.Assist, TuklasChipType.Suggestion,
        TuklasChipType.Filter(false), TuklasChipType.Filter(true),
        TuklasChipType.Input(false), TuklasChipType.Input(true)).forEach { type ->
        TuklasChip(onClick = {}, label = { Text("Illustrative $type") }, type = type)
        TuklasChip(onClick = {}, label = { Text("Disabled $type") }, type = type, enabled = false)
    }
}

@Preview(name = "Fields Light", showBackground = true, widthDp = 360)
@Preview(name = "Fields Dark", showBackground = true, widthDp = 360, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TextFieldPreviews() = PreviewContent {
    TuklasTextFieldVariant.entries.forEach { variant ->
        TuklasTextField("", {}, variant = variant, label = { Text("${variant.name} input") },
            placeholder = { Text("Illustrative placeholder") })
        TuklasTextField("Illustrative value", {}, variant = variant, isError = true,
            label = { Text("Error") }, supportingText = { Text("Illustrative error message") })
        TuklasTextField("Disabled", {}, variant = variant, enabled = false, label = { Text("Disabled") })
    }
}
