package com.lakbay.tuklas.feature.discover.explore.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lakbay.tuklas.core.designsystem.component.TuklasCard
import com.lakbay.tuklas.core.designsystem.component.TuklasImage
import com.lakbay.tuklas.core.designsystem.theme.TuklasSpacing
import com.lakbay.tuklas.feature.discover.shared.model.FeaturedShopUiModel

@Composable
fun FeaturedShopCard(shop: FeaturedShopUiModel, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val metadata = listOfNotNull(shop.category, shop.city).filter { it.isNotBlank() }.joinToString(" · ")
    val announcement = listOfNotNull(metadata, shop.name, shop.highlight?.takeIf { it.isNotBlank() }).joinToString(". ")
    val density = LocalDensity.current
    val fontScale = density.fontScale
    val textShadow = Shadow(Color.Black.copy(alpha = 0.85f), Offset(0f, 1f), blurRadius = 6f)
    TuklasCard(
        modifier = modifier.semantics(mergeDescendants = true) {
            contentDescription = announcement
            onClick(label = "Open ${shop.name}", action = null)
        },
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
    ) {
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            // Keep the photo ratio normally, allowing room for readable text at large system fonts.
            val height = maxOf(maxWidth / 1.45f, 160.dp * fontScale)
            Box(Modifier.fillMaxWidth().height(height)) {
                TuklasImage(shop.imageUrl, Modifier.matchParentSize(), shop.imageResource)
                Box(Modifier.matchParentSize().background(Brush.verticalGradient(
                    // Leave the photograph untouched above the localized bottom fade.
                    0.00f to Color.Transparent,
                    0.45f to Color.Transparent,
                    0.63f to Color.Black.copy(alpha = 0.01f),
                    1.00f to Color.Black.copy(alpha = 0.12f),
                )))
                Column(
                    Modifier.align(Alignment.BottomStart).fillMaxWidth()
                        .padding(start = TuklasSpacing.large, end = TuklasSpacing.large,
                            bottom = TuklasSpacing.large + TuklasSpacing.extraSmall)
                        .clearAndSetSemantics { },
                    verticalArrangement = Arrangement.Top,
                ) {
                    Text(metadata,
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.labelMedium.copy(shadow = textShadow),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis)

                    Spacer(Modifier.height(TuklasSpacing.extraSmall))

                    Text(shop.name,
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge.copy(shadow = textShadow),
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}