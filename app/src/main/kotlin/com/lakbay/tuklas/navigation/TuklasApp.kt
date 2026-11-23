package com.lakbay.tuklas.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.lakbay.tuklas.auth.login.LoginViewModel

@Composable
internal fun TuklasApp(auth: LoginViewModel) {
    val state by auth.state.collectAsStateWithLifecycle()
    TuklasNavHost(rememberNavController(), auth, state)
}