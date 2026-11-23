package com.lakbay.tuklas.profile

import com.lakbay.tuklas.auth.domain.AuthUser
import com.lakbay.tuklas.app.R
import com.lakbay.tuklas.auth.login.LoginUiState
import com.lakbay.tuklas.auth.login.shouldReturnToProfile
import org.junit.Assert.*
import org.junit.Test

class ProfileStateMapperTest {
    @Test fun restorationProjectsLoadingRatherThanGuestContent() {
        assertTrue(LoginUiState().toProfileUiState().isLoading)
    }
    @Test fun guestProjectionDoesNotExposeLoginFailureOrCredentials() {
        val state = LoginUiState(restoringSession = false, operation = LoginUiState.Operation.SIGNING_OUT, error = R.string.auth_error_sign_in).toProfileUiState("Sign-in failure")
        assertNull(state.account); assertNull(state.error); assertTrue(state.isSigningOut)
    }
    @Test fun accountProjectionPreservesDisplayFieldsAndSignOutState() {
        val state = LoginUiState(restoringSession = false, user = AuthUser("private-uid", "Member", "member@example.test", "https://example.test/photo"), operation = LoginUiState.Operation.SIGNING_OUT).toProfileUiState()
        assertEquals("Member", state.account?.displayName)
        assertEquals("member@example.test", state.account?.email)
        assertEquals("https://example.test/photo", state.account?.photoUrl)
        assertTrue(state.isSigningOut)
    }
    @Test fun successfulLoginReturnsOnlyAfterResolutionAndOnceConsumed() {
        val success = LoginUiState(restoringSession = false, user = AuthUser("sample", null))
        assertTrue(success.shouldReturnToProfile(false))
        assertFalse(success.shouldReturnToProfile(true))
        assertFalse(success.copy(operation = LoginUiState.Operation.SIGNING_OUT).shouldReturnToProfile(false))
        assertFalse(success.copy(restoringSession = true).shouldReturnToProfile(false))
        assertFalse(success.copy(user = null).shouldReturnToProfile(false))
    }

    @Test fun signedOutSessionRemainsBusyUntilCleanupFinishesAndThenExposesCleanupFailure() {
        val clearing = LoginUiState(restoringSession = false, operation = LoginUiState.Operation.SIGNING_OUT)
        assertTrue(clearing.toProfileUiState().isSigningOut)
        assertNull(clearing.toProfileUiState().account)
        val failed = clearing.copy(operation = LoginUiState.Operation.IDLE, signOutError = R.string.auth_error_cleanup)
        assertFalse(failed.toProfileUiState().isSigningOut)
        assertEquals("Cleanup failed", failed.toProfileUiState("Cleanup failed").error)
    }
}
