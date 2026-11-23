package com.lakbay.tuklas.feature.discover.shared.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lakbay.tuklas.core.designsystem.component.TuklasImage
import com.lakbay.tuklas.core.designsystem.theme.TuklasSpacing
import com.lakbay.tuklas.domain.model.Place
import com.lakbay.tuklas.feature.discover.shared.mapper.toFeaturedShopUiModel

/** Shared transparent row. List consumers supply the 16dp gap between rows. */
@Composable
fun ExploreShopCard(place: Place, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val display = place.toFeaturedShopUiModel()
    val metadata = exploreShopMetadata(display.category, display.city)
    val colors = MaterialTheme.colorScheme
    Row(
        modifier.fillMaxWidth()
            .clickable(role = Role.Button, onClickLabel = "Open ${place.name}", onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(TuklasSpacing.medium),
    ) {
        TuklasImage(place.imageUrl, Modifier.size(100.dp).clip(MaterialTheme.shapes.medium))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(TuklasSpacing.extraSmall)) {
            Text(place.name, style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold, color = colors.onSurface,
                maxLines = 2, overflow = TextOverflow.Ellipsis)
            metadata?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, color = colors.onSurfaceVariant,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            display.highlight?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, color = colors.onSurfaceVariant,
                    maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

internal fun exploreShopMetadata(category: String?, city: String?): String? =
    listOfNotNull(category?.trim()?.takeIf { it.isNotEmpty() }, city?.trim()?.takeIf { it.isNotEmpty() })
        .joinToString(" · ").takeIf { it.isNotEmpty() }