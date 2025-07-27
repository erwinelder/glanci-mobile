package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.auth.domain.repository.AuthRepository
import com.ataglance.walletglance.request.domain.model.result.Result
import com.ataglance.walletglance.request.domain.model.result.error.AuthError
import com.ataglance.walletglance.request.domain.model.result.success.AuthSuccess

class UpdatePasswordUseCaseImpl(
    private val authRepository: AuthRepository
) : UpdatePasswordUseCase {

    override suspend fun execute(
        password: String,
        newPassword: String
    ): Result<AuthSuccess, AuthError> {
        val result = authRepository.updatePassword(password = password, newPassword = newPassword)

        return result.toResult(success = AuthSuccess.PasswordUpdated)
    }

}