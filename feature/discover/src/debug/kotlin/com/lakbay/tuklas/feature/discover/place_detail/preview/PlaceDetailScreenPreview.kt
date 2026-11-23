package com.lakbay.tuklas.feature.discover.place_detail.preview

import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme
import com.lakbay.tuklas.feature.discover.place_detail.PlaceDetailScreen
import com.lakbay.tuklas.feature.discover.place_detail.PlaceDetailUiState

@Preview(name = "PlaceDetail Light", showBackground = true, widthDp = 400, heightDp = 800,
    uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "PlaceDetail Dark", showBackground = true, widthDp = 400, heightDp = 800,
    uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PlaceDetailScreenPreview() {
    TuklasTheme(dynamicColor = false) {
        Surface { PlaceDetailScreen(state = PlaceDetailUiState("preview-place"), onBack = {}) }
    }
}