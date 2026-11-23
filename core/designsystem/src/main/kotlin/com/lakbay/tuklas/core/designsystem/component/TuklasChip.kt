package com.lakbay.tuklas.core.designsystem.component

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.InputChip
import androidx.compose.material3.SuggestionChip
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/** Selection is available only for the two Material chip types that support it. */
sealed interface TuklasChipType {
    data object Assist : TuklasChipType
    data class Filter(val selected: Boolean, val emphasized: Boolean = false) : TuklasChipType
    data class Input(val selected: Boolean) : TuklasChipType
    data object Suggestion : TuklasChipType
}

/** Hoist selection and click behavior to the caller. Icons augment, rather than replace, [label]. */
@Composable
fun TuklasChip(
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    type: TuklasChipType = TuklasChipType.Assist,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    when (type) {
        TuklasChipType.Assist -> AssistChip(
            onClick, label, modifier, enabled, leadingIcon = leadingIcon,
        )
        is TuklasChipType.Filter -> FilterChip(
            selected = type.selected, onClick = onClick, label = label,
            modifier = modifier, enabled = enabled, leadingIcon = leadingIcon,
            shape = if (type.emphasized) CircleShape else FilterChipDefaults.shape,
            colors = if (type.emphasized) FilterChipDefaults.filterChipColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                labelColor = MaterialTheme.colorScheme.onSurface,
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
                selectedTrailingIconColor = MaterialTheme.colorScheme.onPrimary,
            ) else FilterChipDefaults.filterChipColors(),
        )
        is TuklasChipType.Input -> InputChip(
            selected = type.selected, onClick = onClick, label = label,
            modifier = modifier, enabled = enabled, leadingIcon = leadingIcon,
        )
        TuklasChipType.Suggestion -> SuggestionChip(
            onClick, label, modifier, enabled, icon = leadingIcon,
        )
    }
}
