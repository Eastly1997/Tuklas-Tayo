package com.lakbay.tuklas.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lakbay.tuklas.core.designsystem.theme.TuklasSpacing

/** Contiguous lazy-list segments render one grouped surface without nesting vertical lists. */
enum class TuklasSectionPart { Whole, Top, Middle, Bottom }

/**
 * Use Whole for a normal content slot. For lazy content, place Top/Middle/Bottom
 * consecutively with no inter-item gap and identical width. Rows remain virtualized.
 */
@Composable
fun TuklasSectionContainer(
    modifier: Modifier = Modifier,
    part: TuklasSectionPart = TuklasSectionPart.Whole,
    content: @Composable ColumnScope.() -> Unit,
) {
    val top = part == TuklasSectionPart.Whole || part == TuklasSectionPart.Top
    val bottom = part == TuklasSectionPart.Whole || part == TuklasSectionPart.Bottom
    val corner = 28.dp
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(
            topStart = if (top) corner else 0.dp, topEnd = if (top) corner else 0.dp,
            bottomStart = if (bottom) corner else 0.dp, bottomEnd = if (bottom) corner else 0.dp,
        ),
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Column(
            Modifier.fillMaxWidth().padding(
                start = TuklasSpacing.large, end = TuklasSpacing.large,
                top = if (top) TuklasSpacing.large + TuklasSpacing.extraSmall else 0.dp,
                bottom = if (bottom) TuklasSpacing.large + TuklasSpacing.extraSmall else 0.dp,
            ),
            content = content,
        )
    }
}