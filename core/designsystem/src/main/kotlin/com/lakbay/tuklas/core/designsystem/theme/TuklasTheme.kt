package com.lakbay.tuklas.core.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/** Branded by default; wallpaper-derived colors are an explicit Android 12+ opt-in. */
@Composable
fun TuklasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        if (darkTheme) tuklasDarkColorScheme else tuklasLightColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = tuklasTypography,
        shapes = tuklasShapes,
        content = content,
    )
}
