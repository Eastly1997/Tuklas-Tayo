package com.lakbay.tuklas.core.designsystem.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lakbay.tuklas.core.designsystem.theme.TuklasSpacing
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme

@Preview(name = "Search Trigger Light", widthDp = 400, showBackground = true)
@Preview(name = "Search Trigger Dark", widthDp = 400, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TuklasSearchTriggerPreview() {
    TuklasTheme(dynamicColor = false) {
        Surface { TuklasSearchTrigger({}, Modifier.fillMaxWidth().padding(TuklasSpacing.large)) }
    }
}