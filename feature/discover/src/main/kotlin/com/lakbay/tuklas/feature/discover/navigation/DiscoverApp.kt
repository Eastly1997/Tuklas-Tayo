package com.lakbay.tuklas.feature.discover.navigation

import com.lakbay.tuklas.core.designsystem.R as DesignSystemR

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.IconButton
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lakbay.tuklas.feature.discover.R
import com.lakbay.tuklas.feature.discover.presentation.profile.ProfileUiState

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DiscoverApp(developmentNotice: String? = null, profileState: ProfileUiState, onSignInClick: () -> Unit, onSignOutClick: () -> Unit) {
    val navController = rememberNavController()
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val entry by navController.currentBackStackEntryAsState()
    val currentRoute = entry?.destination?.route
    val tabs = listOf(
        TopLevelDestination(DiscoverDestination.Discover, R.string.navigation_discover, R.drawable.ic_discover_filled, R.drawable.ic_discover_outlined),
        TopLevelDestination(DiscoverDestination.Saved, R.string.navigation_saved, R.drawable.ic_saved_filled, R.drawable.ic_saved_outlined),
        TopLevelDestination(DiscoverDestination.Profile, R.string.navigation_profile, R.drawable.ic_profile_filled, R.drawable.ic_profile_outlined),
    ) + listOfNotNull(developmentTab)
    Scaffold(
        topBar = {
            when (currentRoute) {
                DiscoverDestination.Discover -> TopAppBar(title = { Text(stringResource(R.string.navigation_discover)) })
                DiscoverDestination.Profile -> TopAppBar(title = { Text(stringResource(R.string.navigation_profile)) })
                DiscoverDestination.Search -> TopAppBar(
                    title = { Text(stringResource(R.string.search_title)) },
                    navigationIcon = {
                        IconButton(onClick = {
                            keyboard?.hide()
                            focusManager.clearFocus()
                            navController.popBackStack()
                        }) {
                            Icon(painterResource(R.drawable.ic_arrow_back), contentDescription = stringResource(DesignSystemR.string.action_back))
                        }
                    },
                )
            }
        },
        bottomBar = {
            if (tabs.any { it.route == currentRoute }) {
                NavigationBar {
                    tabs.forEach { tab ->
                        val selected = currentRoute == tab.route
                        NavigationBarItem(
                            selected = selected,
                            colors = if (tab.route == DiscoverDestination.Profile) NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            ) else NavigationBarItemDefaults.colors(),
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
        DiscoverNavHost(navController, Modifier.padding(padding).consumeWindowInsets(padding), developmentNotice, profileState, onSignInClick, onSignOutClick)
    }
}
