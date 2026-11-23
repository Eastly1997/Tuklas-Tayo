package com.lakbay.tuklas.designgallery

import com.lakbay.tuklas.designgallery.section.FeaturedShopsGallerySection
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import com.lakbay.tuklas.di.developmentPlaces
import com.lakbay.tuklas.domain.model.PlaceCategory
import com.lakbay.tuklas.feature.discover.explore.component.exploreShopsSection
import com.lakbay.tuklas.designgallery.section.SectionContainerGallerySection
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import com.lakbay.tuklas.designgallery.section.AnnouncementGallerySection
import com.lakbay.tuklas.designgallery.section.ButtonGallerySection
import com.lakbay.tuklas.designgallery.section.CardGallerySection
import com.lakbay.tuklas.designgallery.section.ChipGallerySection
import com.lakbay.tuklas.designgallery.section.ColorGallerySection
import com.lakbay.tuklas.designgallery.section.MaterialReferenceSection
import com.lakbay.tuklas.designgallery.section.ShapeGallerySection
import com.lakbay.tuklas.designgallery.section.SearchFieldGallerySection
import com.lakbay.tuklas.designgallery.section.TextFieldGallerySection
import com.lakbay.tuklas.designgallery.section.TypographyGallerySection
import com.lakbay.tuklas.core.designsystem.component.TuklasChip
import com.lakbay.tuklas.core.designsystem.component.TuklasChipType
import com.lakbay.tuklas.core.designsystem.theme.TuklasSpacing
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme
import kotlinx.coroutines.launch

internal enum class GalleryThemeMode { LIGHT, DARK, SYSTEM }

@Composable
internal fun DesignGalleryScreen(initialThemeMode: GalleryThemeMode = GalleryThemeMode.SYSTEM) {
    var mode by rememberSaveable { mutableStateOf(initialThemeMode) }
    var galleryCategory by rememberSaveable { mutableStateOf<PlaceCategory?>(null) }
    val darkTheme = when (mode) {
        GalleryThemeMode.LIGHT -> false
        GalleryThemeMode.DARK -> true
        GalleryThemeMode.SYSTEM -> isSystemInDarkTheme()
    }
    TuklasTheme(darkTheme = darkTheme, dynamicColor = false) {
        val snackbar = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        val onAction: (String) -> Unit = { message ->
            scope.launch { snackbar.showSnackbar(message) }
        }
        Scaffold(
            topBar = {
                Column(Modifier.fillMaxWidth().statusBarsPadding().padding(TuklasSpacing.large)) {
                    Text("TuklasTayo UI Kit", style = MaterialTheme.typography.titleLarge)
                    Text("Debug gallery • illustrative examples", style = MaterialTheme.typography.bodySmall)
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(TuklasSpacing.small),
                    ) {
                        GalleryThemeMode.entries.forEach { option ->
                            TuklasChip(
                                onClick = { mode = option },
                                label = { Text(option.name.lowercase().replaceFirstChar { it.titlecase() }) },
                                type = TuklasChipType.Filter(mode == option),
                            )
                        }
                    }
                }
            },
            snackbarHost = { SnackbarHost(snackbar) },
        ) { insets ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(insets).consumeWindowInsets(insets).imePadding()
                    .padding(horizontal = TuklasSpacing.large),
                verticalArrangement = Arrangement.Top,
                contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = TuklasSpacing.extraExtraLarge),
            ) {
                galleryItem("featured_shops") { FeaturedShopsGallerySection(onAction) }
                galleryItem("grouped_sections") { SectionContainerGallerySection() }
                exploreShopsSection(
                    places = developmentPlaces.filter { galleryCategory == null || it.category == galleryCategory },
                    selectedCategory = galleryCategory,
                    onCategorySelected = { galleryCategory = it },
                    onShopClick = { onAction("Selected sample: $it") },
                    )
                item("explore_group_gap") { Spacer(Modifier.height(TuklasSpacing.extraLarge)) }
                galleryItem("colors") { ColorGallerySection() }
                galleryItem("typography") { TypographyGallerySection() }
                galleryItem("shapes") { ShapeGallerySection() }
                galleryItem("buttons") { ButtonGallerySection(onAction) }
                galleryItem("cards") { CardGallerySection(onAction) }
                galleryItem("chips") { ChipGallerySection(onAction) }
                galleryItem("announcements") { AnnouncementGallerySection(onAction) }
                galleryItem("search") { SearchFieldGallerySection(onAction) }
                galleryItem("fields") { TextFieldGallerySection() }
                galleryItem("references") { MaterialReferenceSection() }
            }
        }
    }
}

@Composable
internal fun GallerySection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(TuklasSpacing.medium)) {
        Text(title, Modifier.semantics { heading() }, style = MaterialTheme.typography.headlineSmall)
        content()
    }
}


private fun LazyListScope.galleryItem(key: String, content: @Composable () -> Unit) {
    item(key) {
        Column(Modifier.padding(bottom = TuklasSpacing.extraLarge)) { content() }
    }
}