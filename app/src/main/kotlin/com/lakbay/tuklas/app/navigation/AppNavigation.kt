package com.lakbay.tuklas.app.navigation

import android.net.Uri
import com.lakbay.tuklas.app.saved.presentation.SavedRoute
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import com.lakbay.tuklas.app.BuildConfig
import com.lakbay.tuklas.app.di.developmentDataNotice
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lakbay.tuklas.app.R
import com.lakbay.tuklas.feature.discover.presentation.DiscoverRoute
import com.lakbay.tuklas.feature.explore.ExploreRoute
import com.lakbay.tuklas.feature.placedetail.PlaceDetailRoute

internal data class TopLevelDestination(
    val route: String,
    val labelRes: Int,
    val selectedIconRes: Int,
    val unselectedIconRes: Int,
)

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun TuklasApp() {
    val navController = rememberNavController()
    val entry by navController.currentBackStackEntryAsState()
    val currentRoute = entry?.destination?.route
    val tabs = listOf(
        TopLevelDestination("discover", R.string.navigation_discover, R.drawable.ic_discover_filled, R.drawable.ic_discover_outlined),
        TopLevelDestination("saved", R.string.navigation_saved, R.drawable.ic_saved_filled, R.drawable.ic_saved_outlined),
    ) + listOfNotNull(developmentTab)
    Scaffold(
        topBar = {
            if (currentRoute == "discover") TopAppBar(title = { Text("Discover") })
        },
        bottomBar = {
            if (tabs.any { it.route == currentRoute }) {
                NavigationBar {
                    tabs.forEach { tab ->
                        val selected = currentRoute == tab.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (!selected) {
                                    navController.navigate(tab.route) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            modifier = Modifier.testTag("tab_${tab.route}"),
                            icon = {
                                Icon(
                                    painterResource(if (selected) tab.selectedIconRes else tab.unselectedIconRes),
                                    contentDescription = null, // The item's visible label provides its name.
                                )
                            },
                            label = { Text(stringResource(tab.labelRes)) },
                        )
                    }
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "discover",
            modifier = Modifier.padding(padding).consumeWindowInsets(padding),
        ) {
            composable("discover") {
                DiscoverRoute(
                    developmentNotice = if (!BuildConfig.USE_FIREBASE) developmentDataNotice else null,
                    onExplore = { navController.navigate("explore") { launchSingleTop = true } },
                    onPlaceSelected = { id -> navController.navigate("place/${Uri.encode(id)}") },
                )
            }
            composable("saved") { SavedRoute() }
            composable("explore") { ExploreRoute(onBack = { navController.popBackStack() }) }
            composable("place/{placeId}", arguments = listOf(navArgument("placeId") { type = NavType.StringType })) { detail ->
                PlaceDetailRoute(
                    placeId = requireNotNull(detail.arguments?.getString("placeId")),
                    onBack = { navController.popBackStack() },
                )
            }
            addDevelopmentDestination()
        }
    }
}

