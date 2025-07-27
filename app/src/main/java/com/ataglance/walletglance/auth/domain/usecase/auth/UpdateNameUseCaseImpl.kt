package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.auth.domain.repository.AuthRepository
import com.ataglance.walletglance.request.domain.model.result.Result
import com.ataglance.walletglance.request.domain.model.result.error.AuthError
import com.ataglance.walletglance.request.domain.model.result.success.AuthSuccess

class UpdateNameUseCaseImpl(
    private val authRepository: AuthRepository
) : UpdateNameUseCase {

    override suspend fun execute(name: String): Result<AuthSuccess, AuthError> {
        val result = authRepository.saveUserName(name = name)

        return result.toResult(success = AuthSuccess.EmailUpdated)
    }

}