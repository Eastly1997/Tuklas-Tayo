package com.lakbay.tuklas.auth.login

import com.lakbay.tuklas.auth.AUTH_LOG_TAG

import com.lakbay.tuklas.app.R

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lakbay.tuklas.auth.domain.*
import android.util.Log
import com.lakbay.tuklas.app.BuildConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import com.lakbay.tuklas.auth.login.LoginUiState.Operation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch



class LoginViewModel(
    configured: Boolean,
    observe: ObserveAuthStateUseCase,
    private val signIn: SignInWithGoogleUseCase,
    private val signOut: SignOutUseCase,
) : ViewModel() {
    private val mutableState = MutableStateFlow(LoginUiState(configured = configured))
    val state = mutableState.asStateFlow()
    private var credentialRequest: Job? = null

    init {
        viewModelScope.launch {
            observe().catch { failure ->
                if (failure is CancellationException) throw failure
                if (BuildConfig.DEBUG) Log.e(AUTH_LOG_TAG, "Session observation failed")
                mutableState.update { it.copy(restoringSession = false, error = R.string.auth_error_restore) }
            }.collect { user ->
                if (BuildConfig.DEBUG && (state.value.restoringSession || (state.value.user != null) != (user != null))) {
                    Log.i(AUTH_LOG_TAG, if (user == null) "Signed-out session observed" else "Signed-in session observed")
                }
                mutableState.update { it.copy(user = user, restoringSession = false) }
            }
        }
    }

    /** Only the Activity-dependent picker belongs to the caller's UI scope. */
    fun signInWithGoogle(requestScope: CoroutineScope, fetchToken: suspend () -> String) {
        if (state.value.busy || state.value.restoringSession || !requestScope.isActive) {
            return
        }
        if (!state.value.configured) return
        mutableState.update { it.copy(operation = Operation.REQUESTING_CREDENTIAL, error = null, signOutError = null) }
        val request = requestScope.launch(start = CoroutineStart.UNDISPATCHED) {
            if (BuildConfig.DEBUG) Log.d(AUTH_LOG_TAG, "Google sign-in requested")
            try {
                val token = fetchToken()
                currentCoroutineContext().ensureActive()
                exchangeCredential(token)
            } catch (_: AuthCancelledException) {
                if (BuildConfig.DEBUG) Log.d(AUTH_LOG_TAG, "Google sign-in cancelled")
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (failure: Exception) {
                reportSignInFailure(failure)
            } finally {
                // Recreation interrupts the picker gracefully; it never cancels a Firebase exchange.
                mutableState.update {
                    if (it.operation == Operation.REQUESTING_CREDENTIAL) it.copy(operation = Operation.IDLE) else it
                }
            }
        }
        credentialRequest = request
        request.invokeOnCompletion { if (credentialRequest === request) credentialRequest = null }
    }

    private fun exchangeCredential(token: String) {
        mutableState.update { it.copy(operation = Operation.SIGNING_IN) }
        viewModelScope.launch {
            try {
                signIn(token)
                // The session observer alone publishes identity, including late SDK completion.
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (failure: Exception) {
                reportSignInFailure(failure)
            } finally {
                mutableState.update { it.copy(operation = Operation.IDLE) }
            }
        }
    }

    private fun reportSignInFailure(failure: Exception) {
        if (BuildConfig.DEBUG) {
            if (failure is AuthFailure) Log.w(AUTH_LOG_TAG, "Google sign-in failed: ${failure.kind}")
            else Log.e(AUTH_LOG_TAG, "Google sign-in failed: unexpected error")
        }
        val message = when ((failure as? AuthFailure)?.kind) {
            AuthFailureKind.CONFIGURATION -> R.string.auth_error_configuration
            AuthFailureKind.NO_CREDENTIALS -> R.string.auth_error_no_credentials
            AuthFailureKind.UNSUPPORTED_CREDENTIAL -> R.string.auth_error_unsupported
            AuthFailureKind.INVALID_TOKEN -> R.string.auth_error_invalid_token
            AuthFailureKind.FIREBASE -> R.string.auth_error_firebase
            else -> R.string.auth_error_sign_in
        }
        mutableState.update { it.copy(error = message) }
    }

    fun signOut() {
        if (state.value.busy || state.value.restoringSession) return
        mutableState.update { it.copy(operation = Operation.SIGNING_OUT, error = null, signOutError = null) }
        viewModelScope.launch {
            try {
                signOut.invoke()
            } catch (cancelled: CancellationException) { throw cancelled }
            catch (failure: Exception) {
                if (BuildConfig.DEBUG) {
                    if (failure is AuthFailure) Log.w(AUTH_LOG_TAG, "Sign-out failed: ${failure.kind}")
                    else Log.e(AUTH_LOG_TAG, "Sign-out failed: unexpected error")
                }
                val message = if ((failure as? AuthFailure)?.kind == AuthFailureKind.CREDENTIAL_CLEAR) {
                    R.string.auth_error_cleanup
                } else R.string.auth_error_sign_out
                mutableState.update { it.copy(signOutError = message) }
            } finally { mutableState.update { it.copy(operation = Operation.IDLE) } }
        }
    }

    /** Explicit Login exit cancels only an outstanding picker, never exchange or sign-out. */
    fun cancelSignIn() { credentialRequest?.cancel() }

    override fun onCleared() {
        credentialRequest?.cancel()
        super.onCleared()
    }
}
