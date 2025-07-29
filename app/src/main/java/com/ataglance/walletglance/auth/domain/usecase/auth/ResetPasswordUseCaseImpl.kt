package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.auth.domain.repository.AuthRepository
import com.ataglance.walletglance.core.domain.result.Result
import com.ataglance.walletglance.core.domain.result.error.AuthError
import com.ataglance.walletglance.core.domain.result.success.AuthSuccess

class ResetPasswordUseCaseImpl(
    private val authRepository: AuthRepository
) : ResetPasswordUseCase {

    override suspend fun execute(
        oobCode: String,
        newPassword: String
    ): Result<AuthSuccess, AuthError> {
        val result = authRepository.verifyPasswordReset(oobCode = oobCode, newPassword = newPassword)

        return result.toResult(success = AuthSuccess.PasswordUpdated)
    }

}