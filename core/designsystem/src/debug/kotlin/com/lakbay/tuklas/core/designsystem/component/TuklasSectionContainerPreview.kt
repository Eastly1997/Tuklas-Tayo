package com.lakbay.tuklas.core.designsystem.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lakbay.tuklas.core.designsystem.theme.TuklasSpacing
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme

@Preview(name = "Grouped Sections Light", widthDp = 400, heightDp = 800, showBackground = true)
@Preview(name = "Grouped Sections Dark", widthDp = 400, heightDp = 800, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Grouped Sections Large text", widthDp = 320, heightDp = 1200, showBackground = true, fontScale = 2f)
@Composable
private fun TuklasSectionContainerPreview() {
    TuklasTheme(dynamicColor = false) {
        Column(Modifier.background(MaterialTheme.colorScheme.surfaceContainer).padding(TuklasSpacing.large),
            verticalArrangement = Arrangement.spacedBy(TuklasSpacing.large)) {
            TuklasSectionContainer { Text("Section heading", style = MaterialTheme.typography.titleLarge) }
            TuklasSectionContainer {
                Column(verticalArrangement = Arrangement.spacedBy(TuklasSpacing.large)) {
                    Text("Multiple content items", style = MaterialTheme.typography.titleLarge)
                    Text("First content item")
                    Text("Second content item")
                }
            }
            TuklasSectionContainer {
                Text("Long section heading that wraps naturally", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(TuklasSpacing.medium))
                Text("Long content grows with the available width and the system font size. The surface does not constrain content to a fixed height.")
            }
        }
    }
}