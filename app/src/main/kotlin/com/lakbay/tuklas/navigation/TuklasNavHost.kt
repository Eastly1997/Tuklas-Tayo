package com.lakbay.tuklas.navigation

import com.lakbay.tuklas.auth.AUTH_LOG_TAG

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lakbay.tuklas.app.BuildConfig
import android.util.Log
import com.lakbay.tuklas.auth.login.LoginRoute
import com.lakbay.tuklas.auth.login.LoginUiState
import com.lakbay.tuklas.auth.login.LoginViewModel
import com.lakbay.tuklas.feature.discover.development.developmentDataNotice
import com.lakbay.tuklas.feature.discover.navigation.DiscoverApp
import com.lakbay.tuklas.profile.toProfileUiState



@Composable
internal fun TuklasNavHost(navController: NavHostController, auth: LoginViewModel, state: LoginUiState) {
    NavHost(navController, startDestination = AppDestination.DISCOVER) {
        composable(AppDestination.DISCOVER) {
            val errorResource = state.signOutError ?: state.error.takeIf { state.user != null }
            DiscoverApp(developmentNotice = if (!BuildConfig.USE_FIREBASE) developmentDataNotice else null,
                profileState = state.toProfileUiState(errorResource?.let { stringResource(it) }),
                onSignInClick = { navController.navigate(AppDestination.LOGIN) { launchSingleTop = true } },
                onSignOutClick = auth::signOut)
        }
        composable(AppDestination.LOGIN) {
            LoginRoute(state, auth,
                onBack = { auth.cancelSignIn(); navController.popBackStack() },
                onAuthenticated = {
                    val popped = navController.popBackStack()
                    if (BuildConfig.DEBUG && !popped) Log.w(AUTH_LOG_TAG, "Login return skipped: no previous entry")
                })
        }
        // Manage stays unregistered pending trusted backend authorization.
    }
}
