package com.lakbay.tuklas.feature.discover.explore.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.lakbay.tuklas.core.designsystem.theme.TuklasSpacing
import com.lakbay.tuklas.feature.discover.shared.model.FeaturedShopUiModel

/** Pass the full viewport width; only the heading receives standard screen-edge padding. */
@Composable
fun FeaturedShopsSection(
    shops: List<FeaturedShopUiModel>,
    onShopClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    autoScrollEnabled: Boolean = true,
    autoScrollIntervalMillis: Long = 6_000L,
    carouselState: FeaturedShopsCarouselState = rememberFeaturedShopsCarouselState(),
) {
    if (shops.isEmpty()) return
    val canPause = autoScrollEnabled && shops.size > 1
    Column(modifier, verticalArrangement = Arrangement.spacedBy(TuklasSpacing.medium)) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = TuklasSpacing.large)
                .heightIn(min = if (canPause) 48.dp else 0.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Featured Shops", Modifier.weight(1f).semantics {
                heading()
                if (canPause) {
                    stateDescription = if (carouselState.isPaused) "Automatic advancement paused" else "Automatic advancement enabled"
                    customActions = listOf(CustomAccessibilityAction(
                        label = if (carouselState.isPaused) "Resume automatic featured shops" else "Pause automatic featured shops",
                        action = {
                            carouselState.isPaused = !carouselState.isPaused
                            true
                        },
                    ))
                }
            }, style = MaterialTheme.typography.titleLarge)
        }
        FeaturedShopsCarousel(shops, onShopClick, autoScrollEnabled = autoScrollEnabled,
            autoScrollIntervalMillis = autoScrollIntervalMillis, state = carouselState)
    }
}