package com.lakbay.tuklas.feature.discover.search.preview

import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme
import com.lakbay.tuklas.feature.discover.domain.model.Place
import com.lakbay.tuklas.feature.discover.domain.model.PlaceCategory
import com.lakbay.tuklas.feature.discover.search.SearchScreen
import com.lakbay.tuklas.feature.discover.search.SearchUiState

internal class SearchPreviewData : PreviewParameterProvider<SearchUiState> {
    override val values = sequenceOf(
        SearchUiState.Content("", emptyList()),
        SearchUiState.Loading("café"),
        SearchUiState.Content("café", listOf(
            Place("preview", "Sample Café", PlaceCategory.CAFE, "Sample City", "Illustrative development sample"),
        )),
        SearchUiState.Content("no match", emptyList()),
        SearchUiState.Error("café"),
    )
}

@Preview(name = "Search Light", widthDp = 400, heightDp = 800, showBackground = true)
@Preview(name = "Search Dark", widthDp = 400, heightDp = 800, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Search Large text", widthDp = 400, heightDp = 900, showBackground = true, fontScale = 2f)
@Composable
private fun SearchScreenPreview(@PreviewParameter(SearchPreviewData::class) state: SearchUiState) {
    TuklasTheme(dynamicColor = false) { Surface { SearchScreen(state, {}, {}) } }
}