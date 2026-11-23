package com.lakbay.tuklas.core.designsystem.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lakbay.tuklas.core.designsystem.theme.TuklasSpacing
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme

@Preview(name = "Announcements Light", showBackground = true, widthDp = 400, heightDp = 700)
@Preview(name = "Announcements Dark", showBackground = true, widthDp = 400, heightDp = 700,
    uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Announcements Large text", showBackground = true, widthDp = 400, heightDp = 1100, fontScale = 2f)
@Composable
private fun TuklasAnnouncementBannerPreview() {
    TuklasTheme(dynamicColor = false) {
        Surface {
            Column(Modifier.padding(TuklasSpacing.large),
                verticalArrangement = Arrangement.spacedBy(TuklasSpacing.large)) {
                TuklasAnnouncementBanner("Welcome to TuklasTayo!", "Discover local favorites around Malolos.")
                TuklasAnnouncementBanner("Explore local favorites", "Illustrative clickable preview.", onClick = {})
                TuklasAnnouncementBanner("Development announcement")
                TuklasAnnouncementBanner(
                    "A deliberately long development announcement title that is constrained to two lines",
                    "Illustrative description with additional details to demonstrate graceful two-line truncation at different font sizes.",
                )
            }
        }
    }
}