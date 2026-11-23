package com.lakbay.tuklas.auth.login

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme

@Preview(name = "Login Light", showBackground = true, widthDp = 360, heightDp = 720)
@Preview(name = "Login Dark", showBackground = true, widthDp = 360, heightDp = 720, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LoginPreview() {
    TuklasTheme { LoginScreen(LoginUiState(restoringSession = false, configured = true), {}, {}) }
}