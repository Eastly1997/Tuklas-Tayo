package com.lakbay.tuklas.feature.discover.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lakbay.tuklas.feature.discover.R
import com.lakbay.tuklas.feature.discover.designgallery.DesignGalleryScreen

private const val DebugDestination = "debug"

internal val developmentTab = TopLevelDestination(
    route = DebugDestination,
    labelRes = R.string.navigation_debug,
    selectedIconRes = R.drawable.ic_debug_filled,
    unselectedIconRes = R.drawable.ic_debug_outlined,
)

internal fun NavGraphBuilder.addDevelopmentDestination() {
    composable(developmentTab.route) { DesignGalleryScreen() }
}
