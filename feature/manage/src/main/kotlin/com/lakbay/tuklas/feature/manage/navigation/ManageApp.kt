package com.lakbay.tuklas.feature.manage.navigation

import com.lakbay.tuklas.feature.manage.R

import androidx.compose.ui.res.stringResource

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lakbay.tuklas.feature.manage.dashboard.DashboardScreen

private const val DASHBOARD_ROUTE = "dashboard"

/** App may enter this experience only after verified restaurant authorization. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageApp() {
    val navController = rememberNavController()
    Scaffold(topBar = { TopAppBar(title = { Text(stringResource(R.string.dashboard_title)) }) }) { padding ->
        NavHost(navController, startDestination = DASHBOARD_ROUTE,
            modifier = Modifier.padding(padding).consumeWindowInsets(padding)) {
            composable(DASHBOARD_ROUTE) { DashboardScreen() }
        }
    }
}
