package com.lakbay.tuklas.auth.login

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.BackHandler
import com.lakbay.tuklas.auth.data.requestGoogleIdToken

internal fun LoginUiState.shouldReturnToProfile(alreadyReturned: Boolean): Boolean =
    user != null && !restoringSession && !busy && !alreadyReturned

@Composable
internal fun LoginRoute(state: LoginUiState, auth: LoginViewModel, onBack: () -> Unit, onAuthenticated: () -> Unit) {
    val context = LocalContext.current
    val requestScope = rememberCoroutineScope()
    var returned by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(state.user, state.busy, state.restoringSession) {
        if (state.shouldReturnToProfile(returned)) {
            returned = true
            onAuthenticated()
        }
    }
    BackHandler(onBack = onBack)
    LoginScreen(state,
        onGoogleSignIn = { auth.signInWithGoogle(requestScope) { requestGoogleIdToken(context) } },
        onBack = onBack)
}
