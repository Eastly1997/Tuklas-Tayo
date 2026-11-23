package com.lakbay.tuklas.auth.login

import com.lakbay.tuklas.auth.domain.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginNavigationTest {
    private val dispatcher = StandardTestDispatcher()
    @Before fun setup() { Dispatchers.setMain(dispatcher) }
    @After fun cleanup() { Dispatchers.resetMain() }
    private class Repository : AuthRepository {
        override val configured = true
        val user = MutableStateFlow<AuthUser?>(null)
        var signInCalls = 0
        override fun observeUser() = user
        override suspend fun signInWithGoogle(idToken: String): AuthUser {
            signInCalls++
            return AuthUser("sample", "Member", "member@example.test", "https://example.test/photo.png").also { user.value = it }
        }
        override suspend fun signOut() { user.value = null }
    }
    @Test fun profileStateUpdatesInPlaceThroughSignInAndSignOut() = runTest(dispatcher) {
        val repo = Repository()
        val vm = LoginViewModel(true, ObserveAuthStateUseCase(repo), SignInWithGoogleUseCase(repo), SignOutUseCase(repo))
        runCurrent(); assertNull(vm.state.value.user); assertEquals(0, repo.signInCalls)
        vm.signInWithGoogle(this) { "token" }; runCurrent()
        assertEquals("member@example.test", vm.state.value.user?.email)
        assertEquals("https://example.test/photo.png", vm.state.value.user?.photoUrl)
        vm.signOut(); runCurrent(); assertNull(vm.state.value.user); assertFalse(vm.state.value.busy)
    }
    @Test fun existingObservedSessionDoesNotStartCredentialRetrieval() = runTest(dispatcher) {
        val repo = Repository().apply { user.value = AuthUser("existing", null) }
        val vm = LoginViewModel(true, ObserveAuthStateUseCase(repo), SignInWithGoogleUseCase(repo), SignOutUseCase(repo))
        runCurrent(); assertEquals("existing", vm.state.value.user?.id); assertEquals(0, repo.signInCalls)
    }
}