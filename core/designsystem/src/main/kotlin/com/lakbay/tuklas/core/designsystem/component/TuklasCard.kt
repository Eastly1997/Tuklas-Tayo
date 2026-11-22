package com.lakbay.tuklas.core.designsystem.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.material3.MaterialTheme

enum class TuklasCardVariant { FILLED, ELEVATED, OUTLINED }

/** Omit [onClick] for a static container. [enabled] applies only to interactive cards.
 * Content padding belongs to the caller; Material supplies the themed container and behavior.
 */
@Composable
fun TuklasCard(
    modifier: Modifier = Modifier,
    variant: TuklasCardVariant = TuklasCardVariant.FILLED,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.medium,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (onClick == null) {
        when (variant) {
            TuklasCardVariant.FILLED -> Card(modifier = modifier, shape = shape, content = content)
            TuklasCardVariant.ELEVATED -> ElevatedCard(modifier = modifier, shape = shape, content = content)
            TuklasCardVariant.OUTLINED -> OutlinedCard(modifier = modifier, shape = shape, content = content)
        }
    } else {
        when (variant) {
            TuklasCardVariant.FILLED -> Card(onClick, modifier, enabled, shape = shape, content = content)
            TuklasCardVariant.ELEVATED -> ElevatedCard(onClick, modifier, enabled, shape = shape, content = content)
            TuklasCardVariant.OUTLINED -> OutlinedCard(onClick, modifier, enabled, shape = shape, content = content)
        }
    }
}
