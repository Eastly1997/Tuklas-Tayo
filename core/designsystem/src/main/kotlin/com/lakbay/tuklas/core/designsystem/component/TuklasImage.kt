package com.lakbay.tuklas.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest

/** Decorative, cropped image with a consistent neutral fallback and a stable Coil request. */
@Composable
fun TuklasImage(imageUrl: String?, modifier: Modifier = Modifier, imageResource: Int? = null) {
    val context = LocalContext.current
    val model = imageResource ?: imageUrl?.takeIf { it.isNotBlank() }
    val request = remember(context, model) {
        ImageRequest.Builder(context).data(model).build()
    }
    val imageModifier = modifier.clearAndSetSemantics { }
    if (LocalInspectionMode.current && imageResource != null) {
        Image(painterResource(imageResource), null, imageModifier, contentScale = ContentScale.Crop)
    } else if (model == null) {
        ImagePlaceholder(imageModifier)
    } else {
        SubcomposeAsyncImage(
            model = request, contentDescription = null, modifier = imageModifier,
            contentScale = ContentScale.Crop,
            loading = {
                ImagePlaceholder(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 2.dp)
                }
            },
            error = { ImagePlaceholder(Modifier.fillMaxSize()) },
        )
    }
}

@Composable
private fun ImagePlaceholder(modifier: Modifier, loading: (@Composable () -> Unit)? = null) {
    Box(modifier.background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) {
        if (loading != null) loading() else {
            // A neutral scenic mark works without remote assets or extra icon dependencies.
            Text("◇", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.displaySmall)
        }
    }
}