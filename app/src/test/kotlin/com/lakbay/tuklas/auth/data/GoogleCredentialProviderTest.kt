package com.lakbay.tuklas.auth.data

import androidx.credentials.exceptions.*
import com.lakbay.tuklas.auth.domain.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class GoogleCredentialProviderTest {
    @Test fun credentialSuccessParsesToken() = runTest {
        val token = retrieveGoogleIdToken({ GoogleCredentialResponse(true) { "secret-token" } })
        assertEquals("secret-token", token)
    }
    @Test fun pickerCancellationIsNotFailure() = runTest {
        try { retrieveGoogleIdToken({ throw GetCredentialCancellationException("sensitive SDK message") }); fail() }
        catch (_: AuthCancelledException) { }
    }
    @Test fun noCredentialExceptionIsNotCancellation() = runTest {
        try { retrieveGoogleIdToken({ throw NoCredentialException("sensitive") }); fail() }
        catch (failure: AuthFailure) { assertEquals(AuthFailureKind.NO_CREDENTIALS, failure.kind) }
    }
    @Test fun providerConfigurationFailureHasSpecificCategory() = runTest {
        try { retrieveGoogleIdToken({ throw GetCredentialProviderConfigurationException("sensitive") }); fail() }
        catch (failure: AuthFailure) { assertEquals(AuthFailureKind.CONFIGURATION, failure.kind) }
    }
    @Test fun unsupportedCredentialNeverInvokesGoogleParser() = runTest {
        try { retrieveGoogleIdToken({ GoogleCredentialResponse(false) { error("Parser must not run") } }); fail() }
        catch (failure: AuthFailure) { assertEquals(AuthFailureKind.UNSUPPORTED_CREDENTIAL, failure.kind) }
    }
    @Test fun parsingFailureDoesNotExposePayload() = runTest {
        try { retrieveGoogleIdToken({ GoogleCredentialResponse(true) { throw IllegalArgumentException("credential secret") } }); fail() }
        catch (failure: AuthFailure) { assertEquals(AuthFailureKind.INVALID_TOKEN, failure.kind); assertNull(failure.message) }
    }
    @Test fun blankTokenIsRejected() = runTest {
        try { retrieveGoogleIdToken({ GoogleCredentialResponse(true) { " " } }); fail() }
        catch (failure: AuthFailure) { assertEquals(AuthFailureKind.INVALID_TOKEN, failure.kind) }
    }
    @Test fun unexpectedCredentialFailureIsNotSwallowed() = runTest {
        try { retrieveGoogleIdToken({ throw IllegalStateException("sensitive") }); fail() }
        catch (failure: AuthFailure) { assertEquals(AuthFailureKind.UNEXPECTED, failure.kind) }
    }
}
