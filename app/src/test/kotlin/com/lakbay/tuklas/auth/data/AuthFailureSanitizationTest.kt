package com.lakbay.tuklas.auth.data

import org.junit.Assert.*
import org.junit.Test

class AuthFailureSanitizationTest {
    @Test fun includesSafeSdkTypeAndKnownFirebaseCode() {
        assertEquals("exception=FirebaseAuthInvalidCredentialsException code=ERROR_INVALID_CREDENTIAL",
            sanitizedAuthFailure("FirebaseAuthInvalidCredentialsException", "ERROR_INVALID_CREDENTIAL"))
    }
    @Test fun rejectsArbitraryCodeAndTypeValues() {
        val result = sanitizedAuthFailure("token=user@example.com", "secret-token")
        assertEquals("exception=UnknownException code=OTHER_AUTH_ERROR", result)
    }
}
