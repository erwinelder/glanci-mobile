package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.auth.domain.model.user.UserContext
import com.ataglance.walletglance.auth.domain.repository.AuthRepository
import com.ataglance.walletglance.core.domain.result.Result
import com.ataglance.walletglance.core.domain.result.error.AuthError
import com.ataglance.walletglance.core.domain.result.success.AuthSuccess

class FinishEmailUpdateUseCaseImpl(
    private val authRepository: AuthRepository,
    private val userContext: UserContext
) : FinishEmailUpdateUseCase {

    override suspend fun execute(
        newEmail: String,
        password: String
    ): Result<AuthSuccess, AuthError> {
        val result = authRepository.finishEmailUpdate(newEmail = newEmail, password = password)

        result.getDataOrNull()?.let { user ->
            userContext.saveUserWithToken(user = user)
        }

        return result.toResult(success = AuthSuccess.EmailUpdated)
    }

}