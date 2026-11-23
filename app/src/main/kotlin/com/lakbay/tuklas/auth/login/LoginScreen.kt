package com.lakbay.tuklas.auth.login

import com.lakbay.tuklas.core.designsystem.R as DesignSystemR

import androidx.compose.ui.res.stringResource

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lakbay.tuklas.app.R
import com.lakbay.tuklas.core.designsystem.theme.TuklasSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LoginScreen(state: LoginUiState, onGoogleSignIn: () -> Unit, onBack: () -> Unit) {
    Scaffold(topBar = { TopAppBar(title = { Text(stringResource(R.string.login_title)) }, navigationIcon = {
        IconButton(onClick = onBack) {
            Icon(painterResource(com.lakbay.tuklas.feature.discover.R.drawable.ic_arrow_back), contentDescription = stringResource(DesignSystemR.string.action_back))
        }
    }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding)
            .verticalScroll(rememberScrollState()).padding(TuklasSpacing.extraLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(TuklasSpacing.large, Alignment.CenterVertically)) {
            Image(painterResource(R.drawable.tuklastayo_logo), contentDescription = stringResource(R.string.login_logo_description),
                modifier = Modifier.size(108.dp), contentScale = ContentScale.Fit)
            Text(stringResource(R.string.login_welcome_title), style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
            Text(stringResource(R.string.login_description),
                style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
            GoogleSignInButton(state.configured && !state.busy && !state.restoringSession, onGoogleSignIn)
            if (!state.configured) Text(stringResource(R.string.login_not_configured), style = MaterialTheme.typography.bodyMedium)
            if (state.busy || state.restoringSession) CircularProgressIndicator(Modifier.size(32.dp))
            state.error?.let { Text(stringResource(it), color = MaterialTheme.colorScheme.error) }
        }
    }
}