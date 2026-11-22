package com.lakbay.tuklas.feature.discover.presentation.component

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import com.lakbay.tuklas.core.designsystem.theme.TuklasSpacing
import com.lakbay.tuklas.feature.discover.presentation.model.FeaturedShopUiModel

/** Caller owns screen-edge padding; an empty collection emits no layout or heading. */
@Composable
fun FeaturedShopsSection(shops: List<FeaturedShopUiModel>, onShopClick: (String) -> Unit, modifier: Modifier = Modifier) {
    if (shops.isEmpty()) return
    Column(modifier, verticalArrangement = Arrangement.spacedBy(TuklasSpacing.medium)) {
        Text("Featured Shops", Modifier.semantics { heading() }, style = MaterialTheme.typography.titleLarge)
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            if (shops.size == 1) {
                FeaturedShopCard(shops.single(), { onShopClick(shops.single().id) }, Modifier.fillMaxWidth())
            } else {
                val cardWidth = maxWidth * 0.87f
                val listState = rememberLazyListState()
                LazyRow(
                    state = listState, flingBehavior = rememberSnapFlingBehavior(listState),
                    modifier = Modifier.fillMaxWidth().testTag("featured_carousel"),
                    horizontalArrangement = Arrangement.spacedBy(TuklasSpacing.medium),
                ) {
                    items(shops, key = { it.id }) { shop ->
                        FeaturedShopCard(shop, { onShopClick(shop.id) }, Modifier.width(cardWidth))
                    }
                }
            }
        }
    }
}