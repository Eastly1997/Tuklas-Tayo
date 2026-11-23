package com.lakbay.tuklas.auth.domain

import kotlinx.coroutines.flow.Flow

data class AuthUser(val id: String, val displayName: String?, val email: String? = null, val photoUrl: String? = null)

interface AuthRepository {
    val configured: Boolean
    fun observeUser(): Flow<AuthUser?>
    suspend fun signInWithGoogle(idToken: String): AuthUser
    suspend fun signOut()
}

class AuthCancelledException : Exception()