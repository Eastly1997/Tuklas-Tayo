package com.lakbay.tuklas.feature.discover.presentation.profile

import androidx.compose.ui.res.stringResource

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lakbay.tuklas.feature.discover.R
import com.lakbay.tuklas.core.designsystem.component.*
import com.lakbay.tuklas.core.designsystem.theme.TuklasSpacing

/** The existing customer Scaffold provides the Profile heading, bars and consumed system insets. */
@Composable
fun ProfileScreen(state: ProfileUiState, onSignInClick: () -> Unit, onSignOutClick: () -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainer),
        contentPadding = PaddingValues(TuklasSpacing.large),
        verticalArrangement = Arrangement.spacedBy(TuklasSpacing.large)) {
        if (state.isLoading || (state.isSigningOut && state.account == null)) {
            item("resolving") { TuklasSectionContainer {
                CircularProgressIndicator()
                Text(if (state.isSigningOut) stringResource(R.string.profile_signing_out) else stringResource(R.string.profile_restoring_session))
            } }
        } else {
            val user = state.account
            if (user == null) {
                item("guest_account") {
                    TuklasSectionContainer(Modifier.fillMaxWidth()) {
                        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(TuklasSpacing.large)) {
                            ProfileAvatar(null)
                            Text(stringResource(R.string.profile_guest_title), style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center)
                            Text(stringResource(R.string.profile_guest_description),
                                style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
                            TuklasButton(onClick = onSignInClick, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.profile_sign_in)) }
                        }
                    }
                }
                item("business_information") {
                    TuklasSectionContainer(Modifier.fillMaxWidth()) {
                        Row(horizontalArrangement = Arrangement.spacedBy(TuklasSpacing.medium)) {
                            Icon(painterResource(R.drawable.ic_store), contentDescription = null, Modifier.size(24.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(TuklasSpacing.small)) {
                                Text(stringResource(R.string.profile_business_title), style = MaterialTheme.typography.titleMedium)
                                Text(stringResource(R.string.profile_business_description),
                                    style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            } else {
                item("account") {
                    TuklasSectionContainer(Modifier.fillMaxWidth()) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(TuklasSpacing.large)) {
                            ProfileAvatar(user.profilePhoto)
                            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(TuklasSpacing.extraSmall)) {
                                Text(user.profileName ?: stringResource(R.string.profile_account_fallback), style = MaterialTheme.typography.titleLarge,
                                    maxLines = 2, overflow = TextOverflow.Ellipsis)
                                user.profileEmail?.let { Text(it, style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 2, overflow = TextOverflow.Ellipsis) }
                            }
                        }
                    }
                }
                item("account_information") {
                    TuklasSectionContainer(Modifier.fillMaxWidth()) {
                        Text(stringResource(R.string.profile_account_information), Modifier.semantics { heading() }, style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(TuklasSpacing.large))
                        user.displayName?.trim()?.takeIf(String::isNotEmpty)?.let {
                            Text(stringResource(R.string.profile_name), style = MaterialTheme.typography.labelLarge)
                            Text(it, style = MaterialTheme.typography.bodyLarge)
                            Spacer(Modifier.height(TuklasSpacing.medium))
                        }
                        user.profileEmail?.let {
                            Text(stringResource(R.string.profile_email), style = MaterialTheme.typography.labelLarge)
                            Text(it, style = MaterialTheme.typography.bodyLarge)
                        }
                        if (user.displayName.isNullOrBlank() && user.profileEmail == null) {
                            Text(stringResource(R.string.profile_details_unavailable), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
                item("sign_out") {
                    TuklasSectionContainer(Modifier.fillMaxWidth()) {
                        TuklasButton(onClick = onSignOutClick, variant = TuklasButtonVariant.TEXT, enabled = !state.isSigningOut,
                            modifier = Modifier.fillMaxWidth()) {
                            Icon(painterResource(R.drawable.ic_logout), contentDescription = null, Modifier.size(24.dp))
                            Spacer(Modifier.width(TuklasSpacing.small))
                            Text(stringResource(R.string.profile_sign_out))
                        }
                        if (state.isSigningOut) CircularProgressIndicator(Modifier.size(24.dp))
                    }
                }
            }
        }
        state.error?.let { error -> item("auth_error") {
            TuklasSectionContainer(Modifier.fillMaxWidth()) { Text(error, color = MaterialTheme.colorScheme.error) }
        } }
    }
}
