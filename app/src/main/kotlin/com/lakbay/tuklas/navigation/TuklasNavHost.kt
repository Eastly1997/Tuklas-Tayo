package com.lakbay.tuklas.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lakbay.tuklas.app.BuildConfig
import com.lakbay.tuklas.di.developmentDataNotice
import com.lakbay.tuklas.feature.discover.explore.ExploreRoute
import com.lakbay.tuklas.feature.discover.search.SearchRoute
import com.lakbay.tuklas.feature.saved.SavedRoute
import com.lakbay.tuklas.feature.place_detail.PlaceDetailRoute

@Composable
internal fun TuklasNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = TuklasDestination.Discover,
        modifier = modifier,
    ) {
        composable(TuklasDestination.Discover) {
            ExploreRoute(
                developmentNotice = if (!BuildConfig.USE_FIREBASE) developmentDataNotice else null,
                onSearch = { navController.navigate(TuklasDestination.Search) { launchSingleTop = true } },
                onPlaceSelected = { id -> navController.navigate(TuklasDestination.placeDetail(id)) { launchSingleTop = true } },
            )
        }
        composable(TuklasDestination.Search) {
            SearchRoute(onPlaceSelected = { id -> navController.navigate(TuklasDestination.placeDetail(id)) { launchSingleTop = true } })
        }
        composable(TuklasDestination.Saved) { SavedRoute() }
        composable(TuklasDestination.PlaceDetail, arguments = listOf(navArgument(TuklasDestination.PlaceId) { type = NavType.StringType })) { detail ->
            PlaceDetailRoute(
                placeId = requireNotNull(detail.arguments?.getString(TuklasDestination.PlaceId)),
                onBack = { navController.popBackStack() },
            )
        }
        addDevelopmentDestination()
    }
}
