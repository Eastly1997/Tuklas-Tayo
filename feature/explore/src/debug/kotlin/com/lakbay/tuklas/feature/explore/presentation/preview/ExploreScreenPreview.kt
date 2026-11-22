package com.lakbay.tuklas.feature.explore.presentation.preview

import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme
import com.lakbay.tuklas.feature.explore.presentation.ExploreScreen
import com.lakbay.tuklas.feature.explore.presentation.contract.ExploreUiState

@Preview(name = "Explore Light", showBackground = true, widthDp = 400, heightDp = 800,
    uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Explore Dark", showBackground = true, widthDp = 400, heightDp = 800,
    uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ExploreScreenPreview() {
    TuklasTheme(dynamicColor = false) {
        Surface { ExploreScreen(state = ExploreUiState(), onBack = {}) }
    }
}