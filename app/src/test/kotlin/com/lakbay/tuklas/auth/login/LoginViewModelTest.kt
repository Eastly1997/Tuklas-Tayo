package com.lakbay.tuklas.auth.login

import com.lakbay.tuklas.auth.domain.*
import com.lakbay.tuklas.app.R
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val user = AuthUser("verified-firebase-uid", "Sample")
    @Before fun setup() { Dispatchers.setMain(dispatcher) }
    @After fun teardown() { Dispatchers.resetMain() }

    private class Repository(initial: AuthUser? = null) : AuthRepository {
        override val configured = true
        val users = MutableStateFlow(initial)
        var requests = 0
        var failure: Exception? = null
        var exchangeGate: CompletableDeferred<Unit>? = null
        var cleanupGate: CompletableDeferred<Unit>? = null
        var cleanupFailure: Exception? = null
        var signOutCalls = 0
        var cleanupCancelled = false
        override fun observeUser() = users
        override suspend fun signInWithGoogle(idToken: String): AuthUser {
            requests++
            exchangeGate?.await()
            failure?.let { throw it }
            return AuthUser("verified-firebase-uid", "Sample").also { users.value = it }
        }
        override suspend fun signOut() {
            signOutCalls++
            users.value = null
            try {
                cleanupGate?.await()
                cleanupFailure?.let { throw it }
            } catch (cancelled: CancellationException) {
                cleanupCancelled = true
                throw cancelled
            }
        }
    }
    private fun model(repository: Repository, configured: Boolean = true) = LoginViewModel(configured,
        ObserveAuthStateUseCase(repository), SignInWithGoogleUseCase(repository), SignOutUseCase(repository))

    @Test fun guestStartupCompletesRestoration() = runTest(dispatcher) {
        val vm = model(Repository()); runCurrent()
        assertFalse(vm.state.value.restoringSession); assertNull(vm.state.value.user)
    }
    @Test fun existingSessionIsRestored() = runTest(dispatcher) {
        val vm = model(Repository(user)); runCurrent(); assertEquals(user, vm.state.value.user)
    }
    @Test fun successfulSignInPublishesFirebaseIdentity() = runTest(dispatcher) {
        val vm = model(Repository()); runCurrent(); vm.signInWithGoogle(this) { "token" }; runCurrent()
        assertEquals(user, vm.state.value.user); assertFalse(vm.state.value.busy)
    }
    @Test fun cancelledPickerDoesNotShowErrorOrCallFirebase() = runTest(dispatcher) {
        val repo = Repository(); val vm = model(repo); runCurrent()
        vm.signInWithGoogle(this) { throw AuthCancelledException() }; runCurrent()
        assertNull(vm.state.value.error); assertNull(vm.state.value.user); assertEquals(0, repo.requests)
    }
    @Test fun failedLoginCanRetry() = runTest(dispatcher) {
        val repo = Repository().apply { failure = IllegalStateException("private SDK detail") }
        val vm = model(repo); runCurrent(); vm.signInWithGoogle(this) { "token" }; runCurrent()
        assertEquals(R.string.auth_error_sign_in, vm.state.value.error)
        repo.failure = null; vm.signInWithGoogle(this) { "token" }; runCurrent()
        assertEquals(user, vm.state.value.user); assertNull(vm.state.value.error)
    }
    @Test fun signOutClearsSession() = runTest(dispatcher) {
        val vm = model(Repository(user)); runCurrent(); vm.signOut(); runCurrent()
        assertNull(vm.state.value.user); assertFalse(vm.state.value.busy)
    }
    @Test fun duplicateSignInDoesNotLaunchAnotherPicker() = runTest(dispatcher) {
        val vm = model(Repository()); runCurrent(); val token = CompletableDeferred<String>(); var pickers = 0
        repeat(2) { vm.signInWithGoogle(this) { pickers++; token.await() } }; runCurrent()
        assertEquals(1, pickers); token.complete("token"); runCurrent(); assertEquals(user, vm.state.value.user)
    }
    @Test fun explicitLoginExitCancelsPendingPicker() = runTest(dispatcher) {
        val repo = Repository(); val vm = model(repo); runCurrent()
        vm.signInWithGoogle(this) { awaitCancellation() }; runCurrent()
        vm.cancelSignIn(); runCurrent()
        assertFalse(vm.state.value.busy); assertNull(vm.state.value.user); assertEquals(0, repo.requests)
    }
    @Test fun unconfiguredBuildNeverStartsGoogleSignIn() = runTest(dispatcher) {
        val vm = model(Repository(), configured = false); runCurrent()
        vm.signInWithGoogle(this) { error("Must not launch") }; runCurrent(); assertNull(vm.state.value.user)
    }
    @Test fun cancelledCoroutineRemainsGuestAndResetsLoading() = runTest(dispatcher) {
        val vm = model(Repository()); runCurrent()
        vm.signInWithGoogle(this) { throw CancellationException() }; runCurrent()
        assertFalse(vm.state.value.busy); assertNull(vm.state.value.error); assertNull(vm.state.value.user)
    }
    @Test fun firebaseFailureIsActionableAndRetryable() = runTest(dispatcher) {
        val repo = Repository().apply { failure = AuthFailure(AuthFailureKind.FIREBASE) }
        val vm = model(repo); runCurrent(); vm.signInWithGoogle(this) { "token" }; runCurrent()
        assertEquals(R.string.auth_error_firebase, vm.state.value.error); assertFalse(vm.state.value.busy)
        repo.failure = null; vm.signInWithGoogle(this) { "token" }; runCurrent()
        assertEquals(user, vm.state.value.user)
    }
    @Test fun missingOAuthConfigurationIsExplainedWithoutSdkDetails() = runTest(dispatcher) {
        val vm = model(Repository()); runCurrent()
        vm.signInWithGoogle(this) { throw AuthFailure(AuthFailureKind.CONFIGURATION) }; runCurrent()
        assertEquals(R.string.auth_error_configuration, vm.state.value.error); assertFalse(vm.state.value.busy)
    }

    @Test fun recreationInterruptsOnlyPickerAndAllowsExplicitRetryWithoutAutomaticRelaunch() = runTest(dispatcher) {
        val repo = Repository(); val vm = model(repo); runCurrent()
        val oldUi = CoroutineScope(SupervisorJob() + dispatcher)
        var pickers = 0
        vm.signInWithGoogle(oldUi) { pickers++; awaitCancellation() }
        assertTrue(vm.state.value.busy)
        oldUi.cancel(); runCurrent()
        assertFalse(vm.state.value.busy); assertNull(vm.state.value.error)
        assertEquals(1, pickers); assertEquals(0, repo.requests)
        vm.signInWithGoogle(this) { pickers++; "token" }; runCurrent()
        assertEquals(2, pickers); assertEquals(user, vm.state.value.user)
    }

    @Test fun firebaseExchangeSurvivesRecreationAndNavigationExitAndRejectsCompetingOperations() = runTest(dispatcher) {
        val gate = CompletableDeferred<Unit>()
        val repo = Repository().apply { exchangeGate = gate }
        val vm = model(repo); runCurrent()
        val oldUi = CoroutineScope(SupervisorJob() + dispatcher)
        vm.signInWithGoogle(oldUi) { "token" }; runCurrent()
        assertEquals(LoginUiState.Operation.SIGNING_IN, vm.state.value.operation)
        oldUi.cancel(); vm.cancelSignIn()
        vm.signInWithGoogle(this) { error("Must not request another credential") }; vm.signOut(); runCurrent()
        assertEquals(1, repo.requests); assertEquals(0, repo.signOutCalls)
        assertTrue(vm.state.value.busy)
        gate.complete(Unit); runCurrent()
        assertEquals(user, vm.state.value.user); assertFalse(vm.state.value.busy)
    }

    @Test fun observerRemainsAuthoritativeOverReturnedAndLateIdentities() = runTest(dispatcher) {
        val users = MutableStateFlow<AuthUser?>(null)
        val repo = object : AuthRepository {
            override val configured = true
            override fun observeUser() = users
            override suspend fun signInWithGoogle(idToken: String) = user
            override suspend fun signOut() = Unit
        }
        val vm = LoginViewModel(true, ObserveAuthStateUseCase(repo), SignInWithGoogleUseCase(repo), SignOutUseCase(repo))
        runCurrent(); vm.signInWithGoogle(this) { "token" }; runCurrent()
        assertNull(vm.state.value.user)
        users.value = user; runCurrent(); assertEquals(user, vm.state.value.user)
        users.value = null; runCurrent(); assertNull(vm.state.value.user)
    }

    @Test fun signOutRemainsBusyUntilCleanupAndCannotBeCancelledByLoginExit() = runTest(dispatcher) {
        val gate = CompletableDeferred<Unit>()
        val repo = Repository(user).apply { cleanupGate = gate }
        val vm = model(repo); runCurrent(); vm.signOut(); runCurrent()
        assertNull(vm.state.value.user)
        assertEquals(LoginUiState.Operation.SIGNING_OUT, vm.state.value.operation)
        vm.cancelSignIn(); vm.signOut()
        vm.signInWithGoogle(this) { error("Sign In must remain blocked") }; runCurrent()
        assertEquals(1, repo.signOutCalls); assertFalse(repo.cleanupCancelled)
        gate.complete(Unit); runCurrent()
        assertFalse(vm.state.value.busy); assertNull(vm.state.value.user)
    }

    @Test fun cleanupFailureKeepsAuthoritativeGuestSessionAndReportsRecovery() = runTest(dispatcher) {
        val repo = Repository(user).apply { cleanupFailure = AuthFailure(AuthFailureKind.CREDENTIAL_CLEAR) }
        val vm = model(repo); runCurrent(); vm.signOut(); runCurrent()
        assertNull(vm.state.value.user); assertFalse(vm.state.value.busy)
        assertEquals(R.string.auth_error_cleanup, vm.state.value.signOutError)
        repo.cleanupFailure = null
        vm.signInWithGoogle(this) { "token" }; runCurrent()
        assertEquals(user, vm.state.value.user); assertNull(vm.state.value.signOutError)
    }

    @Test fun failedSignOutDoesNotInventGuestSession() = runTest(dispatcher) {
        val users = MutableStateFlow<AuthUser?>(user)
        val repo = object : AuthRepository {
            override val configured = true
            override fun observeUser() = users
            override suspend fun signInWithGoogle(idToken: String) = user
            override suspend fun signOut(): Unit = throw IllegalStateException("private SDK error")
        }
        val vm = LoginViewModel(true, ObserveAuthStateUseCase(repo), SignInWithGoogleUseCase(repo), SignOutUseCase(repo))
        runCurrent(); vm.signOut(); runCurrent()
        assertEquals(user, vm.state.value.user); assertFalse(vm.state.value.busy)
        assertEquals(R.string.auth_error_sign_out, vm.state.value.signOutError)
    }
}
