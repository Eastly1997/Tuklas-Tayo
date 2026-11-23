package com.lakbay.tuklas.feature.discover.profile

data class ProfileAccount(val displayName: String?, val email: String? = null, val photoUrl: String? = null)

internal val ProfileAccount.profileName: String? get() = displayName?.trim()?.takeIf(String::isNotEmpty)
internal val ProfileAccount.profileEmail: String? get() = email?.trim()?.takeIf(String::isNotEmpty)
internal val ProfileAccount.profilePhoto: String? get() = photoUrl?.trim()?.takeIf(String::isNotEmpty)
