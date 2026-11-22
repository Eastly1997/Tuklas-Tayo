package com.lakbay.tuklas.app.designgallery.preview

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.lakbay.tuklas.app.designgallery.DesignGalleryScreen
import com.lakbay.tuklas.app.designgallery.GalleryThemeMode

@Preview(name = "Full Gallery — Light", showBackground = true, widthDp = 400, heightDp = 900,
    uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun LightGalleryPreview() { DesignGalleryScreen(GalleryThemeMode.LIGHT) }

@Preview(name = "Full Gallery — Dark", showBackground = true, widthDp = 400, heightDp = 900,
    uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DarkGalleryPreview() { DesignGalleryScreen(GalleryThemeMode.DARK) }
