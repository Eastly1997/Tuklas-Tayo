package com.lakbay.tuklas.auth.login

import com.lakbay.tuklas.auth.domain.AuthUser
import androidx.annotation.StringRes

data class LoginUiState(
    val restoringSession: Boolean = true,
    val configured: Boolean = false,
    val user: AuthUser? = null,
    val operation: Operation = Operation.IDLE,
    @param:StringRes val error: Int? = null,
    @param:StringRes val signOutError: Int? = null,
) {
    val busy: Boolean get() = operation != Operation.IDLE

    enum class Operation { IDLE, REQUESTING_CREDENTIAL, SIGNING_IN, SIGNING_OUT }
}
