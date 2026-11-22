package com.lakbay.tuklas.app.saved.presentation.preview

import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lakbay.tuklas.app.R
import com.lakbay.tuklas.app.saved.presentation.SavedScreen
import com.lakbay.tuklas.app.saved.presentation.contract.SavedUiState
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme

@Preview(name = "Saved Light", showBackground = true, widthDp = 400, heightDp = 800,
    uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Saved Dark", showBackground = true, widthDp = 400, heightDp = 800,
    uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SavedScreenPreview() {
    TuklasTheme(dynamicColor = false) {
        Surface {
            SavedScreen(SavedUiState(stringResource(R.string.navigation_saved), stringResource(R.string.saved_placeholder)))
        }
    }
}