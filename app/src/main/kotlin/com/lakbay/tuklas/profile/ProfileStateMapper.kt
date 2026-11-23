package com.lakbay.tuklas.profile

import com.lakbay.tuklas.auth.login.LoginUiState
import com.lakbay.tuklas.feature.discover.profile.ProfileAccount
import com.lakbay.tuklas.feature.discover.profile.ProfileUiState

/** No app auth type crosses into Discover; this is a derived immutable UI value, not a new session store. */
internal fun LoginUiState.toProfileUiState(errorMessage: String? = null) = ProfileUiState(
    isLoading = restoringSession,
    account = user?.let { ProfileAccount(it.displayName, it.email, it.photoUrl) },
    isSigningOut = operation == LoginUiState.Operation.SIGNING_OUT,
    error = errorMessage.takeIf { signOutError != null || user != null },
)
