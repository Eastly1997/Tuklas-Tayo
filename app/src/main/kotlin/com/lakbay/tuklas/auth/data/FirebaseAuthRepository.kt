package com.lakbay.tuklas.auth.data

import com.lakbay.tuklas.auth.AUTH_LOG_TAG

import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.ClearCredentialException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.lakbay.tuklas.auth.domain.*
import android.util.Log
import com.google.firebase.auth.FirebaseAuthException
import com.lakbay.tuklas.app.BuildConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull



internal class FirebaseAuthRepository(
    private val auth: FirebaseAuth?,
    private val credentials: CredentialManager,
) : AuthRepository {
    override val configured get() = auth != null
    override fun observeUser(): Flow<AuthUser?> = auth?.let { firebase ->
        callbackFlow<AuthUser?> {
            val listener = FirebaseAuth.AuthStateListener {
                trySend(it.currentUser?.let { user -> AuthUser(user.uid, user.displayName, user.email, user.photoUrl?.toString()) })
            }
            firebase.addAuthStateListener(listener)
            awaitClose { firebase.removeAuthStateListener(listener) }
        }
    } ?: flowOf(null)

    override suspend fun signInWithGoogle(idToken: String): AuthUser {
        val firebase = auth ?: run {
            throw AuthFailure(AuthFailureKind.CONFIGURATION)
        }
        if (BuildConfig.DEBUG) Log.d(AUTH_LOG_TAG, "Firebase authentication started")
        try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val user = checkNotNull(firebase.signInWithCredential(credential).await().user)
            if (BuildConfig.DEBUG) Log.i(AUTH_LOG_TAG, "Firebase authentication succeeded")
            return AuthUser(user.uid, user.displayName, user.email, user.photoUrl?.toString())
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (failure: Exception) {
            if (BuildConfig.DEBUG) Log.w(AUTH_LOG_TAG, "Firebase authentication failed: ${sanitizedAuthFailure(failure.javaClass.simpleName, (failure as? FirebaseAuthException)?.errorCode)}")
            throw AuthFailure(AuthFailureKind.FIREBASE)
        }
    }

    override suspend fun signOut() {
        if (BuildConfig.DEBUG) Log.d(AUTH_LOG_TAG, "Sign-out requested")
        auth?.signOut()
        if (BuildConfig.DEBUG) Log.i(AUTH_LOG_TAG, "Firebase sign-out completed")
        try {
            val cleared = withTimeoutOrNull(15_000L) {
                credentials.clearCredentialState(ClearCredentialStateRequest())
                true
            } ?: false
            if (!cleared) {
                if (BuildConfig.DEBUG) Log.w(AUTH_LOG_TAG, "Credential state clearing timed out")
                throw AuthFailure(AuthFailureKind.CREDENTIAL_CLEAR)
            }
        } catch (failure: ClearCredentialException) {
            if (BuildConfig.DEBUG) Log.w(AUTH_LOG_TAG, "Credential state clearing failed: ${sanitizedAuthFailure(failure.javaClass.simpleName, null)}")
            throw AuthFailure(AuthFailureKind.CREDENTIAL_CLEAR)
        }
        if (BuildConfig.DEBUG) Log.d(AUTH_LOG_TAG, "Credential state clearing completed")
    }
}

/** Filters error metadata only; never receives messages, stack traces or credential payloads. */
internal fun sanitizedAuthFailure(exceptionType: String, firebaseCode: String?): String {
    val type = exceptionType.takeIf { it.matches(Regex("[A-Za-z][A-Za-z0-9_]{0,79}")) } ?: "UnknownException"
    val allowedCodes = setOf("ERROR_INVALID_CREDENTIAL", "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL",
        "ERROR_USER_DISABLED", "ERROR_OPERATION_NOT_ALLOWED", "ERROR_INVALID_USER_TOKEN", "ERROR_USER_TOKEN_EXPIRED",
        "ERROR_NETWORK_REQUEST_FAILED", "ERROR_TOO_MANY_REQUESTS", "ERROR_INVALID_API_KEY", "ERROR_APP_NOT_AUTHORIZED",
        "ERROR_WEB_INTERNAL_ERROR", "ERROR_INTERNAL_ERROR", "ERROR_INVALID_CUSTOM_TOKEN", "ERROR_CUSTOM_TOKEN_MISMATCH")
    val code = firebaseCode?.let { if (it in allowedCodes) it else "OTHER_AUTH_ERROR" }
    return "exception=$type" + (code?.let { " code=$it" } ?: "")
}
