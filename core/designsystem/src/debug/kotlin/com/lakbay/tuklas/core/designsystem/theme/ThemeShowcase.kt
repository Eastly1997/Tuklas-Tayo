package com.lakbay.tuklas.core.designsystem.theme

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lakbay.tuklas.core.designsystem.component.TuklasButton
import com.lakbay.tuklas.core.designsystem.component.TuklasButtonVariant
import com.lakbay.tuklas.core.designsystem.component.TuklasCard
import com.lakbay.tuklas.core.designsystem.component.TuklasChip
import com.lakbay.tuklas.core.designsystem.component.TuklasChipType
import com.lakbay.tuklas.core.designsystem.component.TuklasTextField

// Debug-only, preview-only composition: no production entry point or application state.
@Composable
private fun ThemeShowcase() {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.padding(TuklasSpacing.extraLarge),
            verticalArrangement = Arrangement.spacedBy(TuklasSpacing.large),
        ) {
            Text("TuklasTayo", style = MaterialTheme.typography.displaySmall)
            Text("Theme foundation", style = MaterialTheme.typography.headlineMedium)
            Text("Section title", style = MaterialTheme.typography.titleLarge)
            Text("System sans-serif with scalable Material 3 text.", style = MaterialTheme.typography.bodyLarge)
            Text("Supporting information", style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Label text", style = MaterialTheme.typography.labelLarge)
            TuklasButton(onClick = {}) { Text("Primary action") }
            TuklasButton(onClick = {}, variant = TuklasButtonVariant.OUTLINED) { Text("Secondary action") }
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = MaterialTheme.shapes.medium,
            ) {
                Text("Low surface container", Modifier.padding(TuklasSpacing.large))
            }
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = MaterialTheme.shapes.medium,
            ) {
                Text("Surface container", Modifier.padding(TuklasSpacing.large))
            }
            TuklasCard {
                Column(Modifier.fillMaxWidth().padding(TuklasSpacing.large)) {
                    Text("Shared TuklasTayo card", style = MaterialTheme.typography.titleMedium)
                    Text("Shared card container", style = MaterialTheme.typography.bodyMedium)
                }
            }
            TuklasChip(type = TuklasChipType.Filter(true), onClick = {}, label = { Text("Selected filter") })
            TuklasChip(type = TuklasChipType.Filter(false), onClick = {}, label = { Text("Available filter") })
            TuklasTextField(
                value = "Sample text",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                label = { Text("Text field") },
                supportingText = { Text("Preview only") },
            )
        }
    }
}

@Preview(name = "TuklasTayo Light", showBackground = true, widthDp = 400, heightDp = 1100,
    uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun LightThemePreview() {
    TuklasTheme(darkTheme = false, dynamicColor = false) { ThemeShowcase() }
}

@Preview(name = "TuklasTayo Dark", showBackground = true, widthDp = 400, heightDp = 1100,
    uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DarkThemePreview() {
    TuklasTheme(darkTheme = true, dynamicColor = false) { ThemeShowcase() }
}
