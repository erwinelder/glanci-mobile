package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.auth.domain.repository.AuthRepository
import com.ataglance.walletglance.request.domain.model.result.Result
import com.ataglance.walletglance.request.domain.model.result.error.AuthError
import com.ataglance.walletglance.request.domain.model.result.success.AuthSuccess

class RequestEmailUpdateUseCaseImpl(
    private val authRepository: AuthRepository
) : RequestEmailUpdateUseCase {

    override suspend fun execute(
        password: String,
        newEmail: String
    ): Result<AuthSuccess, AuthError> {
        val result = authRepository.requestEmailUpdate(password = password, newEmail = newEmail)

        return result.toResult(success = AuthSuccess.EmailUpdateEmailVerificationSent)
    }

}