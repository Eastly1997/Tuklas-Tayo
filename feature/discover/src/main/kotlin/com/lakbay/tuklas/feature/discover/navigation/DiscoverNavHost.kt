package com.lakbay.tuklas.feature.discover.navigation

import androidx.compose.runtime.Composable
import com.lakbay.tuklas.feature.discover.profile.ProfileUiState
import com.lakbay.tuklas.feature.discover.profile.ProfileScreen
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument


import com.lakbay.tuklas.feature.discover.explore.ExploreRoute
import com.lakbay.tuklas.feature.discover.search.SearchRoute
import com.lakbay.tuklas.feature.discover.saved.SavedRoute
import com.lakbay.tuklas.feature.discover.place_detail.PlaceDetailRoute

@Composable
internal fun DiscoverNavHost(navController: NavHostController, modifier: Modifier = Modifier, developmentNotice: String? = null, profileState: ProfileUiState, onSignInClick: () -> Unit, onSignOutClick: () -> Unit) {
    NavHost(
        navController = navController,
        startDestination = DiscoverDestination.Discover,
        modifier = modifier,
    ) {
        composable(DiscoverDestination.Discover) {
            ExploreRoute(
                developmentNotice = developmentNotice,
                onSearch = { navController.navigate(DiscoverDestination.Search) { launchSingleTop = true } },
                onPlaceSelected = { id -> navController.navigate(DiscoverDestination.placeDetail(id)) { launchSingleTop = true } },
            )
        }
        composable(DiscoverDestination.Search) {
            SearchRoute(onPlaceSelected = { id -> navController.navigate(DiscoverDestination.placeDetail(id)) { launchSingleTop = true } })
        }
        composable(DiscoverDestination.Saved) { SavedRoute() }
        composable(DiscoverDestination.Profile) { ProfileScreen(profileState, onSignInClick, onSignOutClick) }
        composable(DiscoverDestination.PlaceDetail, arguments = listOf(navArgument(DiscoverDestination.PlaceId) { type = NavType.StringType })) { detail ->
            PlaceDetailRoute(
                placeId = requireNotNull(detail.arguments?.getString(DiscoverDestination.PlaceId)),
                onBack = { navController.popBackStack() },
            )
        }
        addDevelopmentDestination()
    }
}
