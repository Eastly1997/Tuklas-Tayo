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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lakbay.tuklas.core.designsystem.R
import com.lakbay.tuklas.core.designsystem.theme.TuklasSpacing

/** Stateless announcement surface; a null action is informational and has no click semantics. */
@Composable
fun TuklasAnnouncementBanner(
    title: String,
    description: String? = null,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val surfaceModifier = modifier.semantics(mergeDescendants = true) { }
    val content: @Composable () -> Unit = {
        Row(
            Modifier.fillMaxWidth().heightIn(min = 72.dp)
                .padding(horizontal = TuklasSpacing.large, vertical = TuklasSpacing.medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(TuklasSpacing.medium),
        ) {
            Icon(painterResource(R.drawable.ic_announcement), contentDescription = null,
                modifier = Modifier.size(24.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(TuklasSpacing.extraSmall)) {
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold,
                    maxLines = 2, overflow = TextOverflow.Ellipsis)
                description?.takeIf { it.isNotBlank() }?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall,
                        maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
            }
            if (onClick != null) {
                Icon(painterResource(R.drawable.ic_announcement_chevron), contentDescription = null,
                    modifier = Modifier.size(24.dp))
            }
        }
    }
    if (onClick == null) {
        Surface(modifier = surfaceModifier, shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary,
            content = content)
    } else {
        Surface(onClick = onClick, modifier = surfaceModifier, shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary,
            content = content)
    }
}