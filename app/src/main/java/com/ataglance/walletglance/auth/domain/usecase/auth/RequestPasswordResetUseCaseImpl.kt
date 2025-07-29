package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.auth.domain.repository.AuthRepository
import com.ataglance.walletglance.core.domain.result.Result
import com.ataglance.walletglance.core.domain.result.error.AuthError
import com.ataglance.walletglance.core.domain.result.success.AuthSuccess

class RequestPasswordResetUseCaseImpl(
    private val authRepository: AuthRepository
) : RequestPasswordResetUseCase {

    override suspend fun execute(email: String): Result<AuthSuccess, AuthError> {
        val result = authRepository.requestPasswordReset(email = email)

        return result.toResult(success = AuthSuccess.ResetPasswordEmailSent)
    }

}