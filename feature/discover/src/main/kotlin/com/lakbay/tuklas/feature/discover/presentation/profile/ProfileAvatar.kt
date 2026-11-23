package com.lakbay.tuklas.feature.discover.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.lakbay.tuklas.feature.discover.R

@Composable
internal fun ProfileAvatar(photoUrl: String?) {
    Box(Modifier.size(64.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceContainerHighest),
        contentAlignment = Alignment.Center) {
        if (photoUrl == null || LocalInspectionMode.current) AccountFallback()
        else SubcomposeAsyncImage(model = photoUrl, contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize(),
            loading = { AccountFallback() }, error = { AccountFallback() })
    }
}

@Composable
private fun AccountFallback() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Icon(painterResource(R.drawable.ic_profile_outlined), contentDescription = null,
            modifier = Modifier.size(36.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}