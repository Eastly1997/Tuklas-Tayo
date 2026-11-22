package com.lakbay.tuklas.app.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lakbay.tuklas.app.R
import com.lakbay.tuklas.app.designgallery.DesignGalleryScreen

internal val developmentTab = TopLevelDestination(
    route = "debug",
    labelRes = R.string.navigation_debug,
    selectedIconRes = R.drawable.ic_debug_filled,
    unselectedIconRes = R.drawable.ic_debug_outlined,
)

internal fun NavGraphBuilder.addDevelopmentDestination() {
    composable(developmentTab.route) { DesignGalleryScreen() }
}
