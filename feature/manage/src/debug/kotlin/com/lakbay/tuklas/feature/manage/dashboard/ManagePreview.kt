package com.lakbay.tuklas.feature.manage.dashboard

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme
import androidx.compose.material3.Surface

@Preview(name = "Manage Light", showBackground = true)
@Preview(name = "Manage Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ManagePreview() {
    TuklasTheme { Surface { DashboardScreen() } }
}