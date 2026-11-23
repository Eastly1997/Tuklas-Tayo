package com.lakbay.tuklas.designgallery.section

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.lakbay.tuklas.designgallery.GallerySection
import com.lakbay.tuklas.core.designsystem.component.TuklasAnnouncementBanner

@Composable
internal fun AnnouncementGallerySection(onAction: (String) -> Unit) {
    GallerySection("Announcement banners") {
        Text("Development samples • illustrative announcements")
        Text("Informational • title and description")
        TuklasAnnouncementBanner("Welcome to TuklasTayo!", "Discover local favorites around Malolos.")
        Text("Clickable • action shows a sample snackbar")
        TuklasAnnouncementBanner("Explore local favorites", "See what the community has to offer.",
            onClick = { onAction("Development announcement selected") })
        Text("Title only")
        TuklasAnnouncementBanner("Welcome to TuklasTayo!")
        Text("Long title and description")
        TuklasAnnouncementBanner(
            "A deliberately long development announcement title with more text than fits in two lines",
            "A longer illustrative description to check wrapping and ellipsis while preserving readable typography at larger system font scales.",
        )
    }
}