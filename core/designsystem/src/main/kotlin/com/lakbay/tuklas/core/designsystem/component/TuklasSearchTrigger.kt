package com.lakbay.tuklas.core.designsystem.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import com.lakbay.tuklas.core.designsystem.R
import com.lakbay.tuklas.core.designsystem.theme.TuklasSpacing

/** A real navigation button, with no text editor, input connection, or keyboard request. */
@Composable
fun TuklasSearchTrigger(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = TuklasSearchDefaults.placeholder,
) {
    Surface(onClick = onClick, modifier = modifier.heightIn(min = TuklasSearchDefaults.minHeight)
        .semantics { role = Role.Button },
        shape = TuklasSearchDefaults.shape, color = TuklasSearchDefaults.containerColor,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ) {
        Row(Modifier.padding(horizontal = TuklasSpacing.large, vertical = TuklasSpacing.medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(TuklasSpacing.medium)) {
            Icon(painterResource(R.drawable.ic_search), null, Modifier.size(TuklasSearchDefaults.iconSize))
            Text(placeholder, style = TuklasSearchDefaults.textStyle)
        }
    }
}