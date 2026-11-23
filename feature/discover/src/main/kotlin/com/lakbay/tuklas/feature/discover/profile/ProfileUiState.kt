package com.lakbay.tuklas.feature.discover.profile

/** Presentation projection only; App owns the authenticated session and its observer. */
data class ProfileUiState(
    val isLoading: Boolean = false,
    val account: ProfileAccount? = null,
    val isSigningOut: Boolean = false,
    val error: String? = null,
)