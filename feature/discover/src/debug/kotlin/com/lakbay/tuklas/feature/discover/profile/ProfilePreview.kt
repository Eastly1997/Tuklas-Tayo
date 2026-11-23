package com.lakbay.tuklas.feature.discover.profile

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme
import com.lakbay.tuklas.feature.discover.presentation.profile.ProfileAccount
import com.lakbay.tuklas.feature.discover.presentation.profile.ProfileScreen
import com.lakbay.tuklas.feature.discover.presentation.profile.ProfileUiState

internal class ProfilePreviewData : PreviewParameterProvider<ProfileUiState> {
    override val values = sequenceOf(
        ProfileUiState(isLoading = true),
        ProfileUiState(isLoading = false),
        ProfileUiState(account = ProfileAccount("Sample Member"), isSigningOut = true),
        ProfileUiState(
            account = ProfileAccount("Sample Member"),
            error = "Couldn't sign out. Please try again."
        ),
        ProfileUiState(
            isLoading = false,
            account = ProfileAccount("Sample Member", "member@example.test")
        ),
        ProfileUiState(isLoading = false, account = ProfileAccount(null)),
        ProfileUiState(
            isLoading = false,
            account = ProfileAccount(
                "A long example member name for accessibility",
                "long-example-email-address@example.test"
            )
        ),
    )
}

@Preview(name = "Profile Light", showBackground = true, widthDp = 360, heightDp = 760)
@Preview(name = "Profile Dark", showBackground = true, widthDp = 360, heightDp = 760, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ProfilePreview(@PreviewParameter(ProfilePreviewData::class) state: ProfileUiState) {
    TuklasTheme { ProfileScreen(state, {}, {}) }
}