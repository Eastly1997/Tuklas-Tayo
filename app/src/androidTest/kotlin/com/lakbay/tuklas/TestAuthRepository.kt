package com.lakbay.tuklas

import com.lakbay.tuklas.auth.domain.AuthRepository
import com.lakbay.tuklas.auth.domain.AuthUser
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow

/** Exists only in the instrumentation APK; never reads a device account or Firebase session. */
internal object TestAuthRepository : AuthRepository {
    override val configured = true
    val users = MutableStateFlow<AuthUser?>(null)
    var exchangeGate: CompletableDeferred<Unit>? = null
    var cleanupGate: CompletableDeferred<Unit>? = null
    val member = AuthUser("test-member", "Test Member", "member@example.test")

    override fun observeUser() = users
    override suspend fun signInWithGoogle(idToken: String): AuthUser {
        exchangeGate?.await()
        return member.also { users.value = it }
    }
    override suspend fun signOut() {
        users.value = null
        cleanupGate?.await()
    }
    fun reset() {
        users.value = null
        exchangeGate = null
        cleanupGate = null
    }
}
