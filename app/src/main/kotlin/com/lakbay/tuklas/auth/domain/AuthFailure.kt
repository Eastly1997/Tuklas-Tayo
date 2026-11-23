package com.lakbay.tuklas.auth.domain

enum class AuthFailureKind { CONFIGURATION, NO_CREDENTIALS, UNSUPPORTED_CREDENTIAL, INVALID_TOKEN, FIREBASE, CREDENTIAL_CLEAR, UNEXPECTED }

/** Carries a safe category only, never a credential, raw SDK message or account data. */
class AuthFailure(val kind: AuthFailureKind) : Exception()
