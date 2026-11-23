package com.lakbay.tuklas.navigation

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
import com.lakbay.tuklas.app.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun TuklasApp() {
    val navController = rememberNavController()
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val entry by navController.currentBackStackEntryAsState()
    val currentRoute = entry?.destination?.route
    val tabs = listOf(
        TopLevelDestination(TuklasDestination.Discover, R.string.navigation_discover, R.drawable.ic_discover_filled, R.drawable.ic_discover_outlined),
        TopLevelDestination(TuklasDestination.Saved, R.string.navigation_saved, R.drawable.ic_saved_filled, R.drawable.ic_saved_outlined),
    ) + listOfNotNull(developmentTab)
    Scaffold(
        topBar = {
            when (currentRoute) {
                TuklasDestination.Discover -> TopAppBar(title = { Text("Discover") })
                TuklasDestination.Search -> TopAppBar(
                    title = { Text("Search Places") },
                    navigationIcon = {
                        IconButton(onClick = {
                            keyboard?.hide()
                            focusManager.clearFocus()
                            navController.popBackStack()
                        }) {
                            Icon(painterResource(R.drawable.ic_arrow_back), contentDescription = "Back")
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
        TuklasNavHost(navController, Modifier.padding(padding).consumeWindowInsets(padding))
    }
}
