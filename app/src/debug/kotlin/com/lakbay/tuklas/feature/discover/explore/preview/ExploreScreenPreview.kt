package com.lakbay.tuklas.feature.discover.explore.preview

import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme
import com.lakbay.tuklas.feature.discover.explore.ExploreScreen
import com.lakbay.tuklas.feature.discover.explore.ExploreUiState

@Preview(name = "Discover Light", showBackground = true, widthDp = 400, heightDp = 900,
    uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Discover Dark", showBackground = true, widthDp = 400, heightDp = 900,
    uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ExploreScreenPreview(@PreviewParameter(ExplorePreviewData::class) state: ExploreUiState) {
    TuklasTheme(dynamicColor = false) {
        Surface {
            ExploreScreen(state, onIntent = {}, onPlaceSelected = {},
                developmentNotice = "Preview samples — fictional businesses")
        }
    }
}