package com.lakbay.tuklas.core.designsystem.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

enum class TuklasButtonVariant { FILLED, FILLED_TONAL, OUTLINED, ELEVATED, TEXT }

/** Material owns colors, shape, typography, touch targets, focus and disabled semantics.
 * Supply a readable label in [content]; decorative icons should have no content description.
 */
@Composable
fun TuklasButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: TuklasButtonVariant = TuklasButtonVariant.FILLED,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    when (variant) {
        TuklasButtonVariant.FILLED -> Button(onClick, modifier, enabled, content = content)
        TuklasButtonVariant.FILLED_TONAL -> FilledTonalButton(onClick, modifier, enabled, content = content)
        TuklasButtonVariant.OUTLINED -> OutlinedButton(onClick, modifier, enabled, content = content)
        TuklasButtonVariant.ELEVATED -> ElevatedButton(onClick, modifier, enabled, content = content)
        TuklasButtonVariant.TEXT -> TextButton(onClick, modifier, enabled, content = content)
    }
}
