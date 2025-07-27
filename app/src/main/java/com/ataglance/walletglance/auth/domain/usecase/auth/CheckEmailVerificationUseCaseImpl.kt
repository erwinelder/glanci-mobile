package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.auth.domain.model.user.UserContext
import com.ataglance.walletglance.auth.domain.repository.AuthRepository
import com.ataglance.walletglance.request.domain.model.result.Result
import com.ataglance.walletglance.request.domain.model.result.error.AuthError
import com.ataglance.walletglance.request.domain.model.result.success.AuthSuccess

class CheckEmailVerificationUseCaseImpl(
    private val authRepository: AuthRepository,
    private val userContext: UserContext
) : CheckEmailVerificationUseCase {

    override suspend fun execute(email: String, password: String): Result<AuthSuccess, AuthError> {
        val result = authRepository.signIn(email = email, password = password)

        result.getDataOrNull()?.let { user ->
            userContext.saveUserWithToken(user = user)
        }

        return result.toResult(success = AuthSuccess.SignedUp)
    }

}