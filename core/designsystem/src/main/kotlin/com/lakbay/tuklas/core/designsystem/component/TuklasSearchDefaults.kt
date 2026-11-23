package com.lakbay.tuklas.core.designsystem.component

import com.lakbay.tuklas.core.designsystem.R

import androidx.compose.ui.res.stringResource

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

/** Shared appearance for the navigation trigger and editable search input. */
internal object TuklasSearchDefaults {
    val placeholder: String @Composable get() = stringResource(R.string.search_placeholder)
    val shape = CircleShape
    val minHeight = 56.dp
    val iconSize = 24.dp
    val containerColor @Composable get() = MaterialTheme.colorScheme.surfaceVariant
    val textStyle @Composable get() = MaterialTheme.typography.bodyLarge
}