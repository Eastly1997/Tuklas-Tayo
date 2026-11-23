package com.lakbay.tuklas.auth.domain

class ObserveAuthStateUseCase(private val repository: AuthRepository) {
    operator fun invoke() = repository.observeUser()
}
class SignInWithGoogleUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(token: String): AuthUser {
        require(token.isNotBlank())
        return repository.signInWithGoogle(token)
    }
}
class SignOutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.signOut()
}