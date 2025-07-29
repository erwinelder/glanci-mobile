package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.auth.domain.model.user.UserContext
import com.ataglance.walletglance.auth.domain.repository.AuthRepository
import com.ataglance.walletglance.core.domain.result.Result
import com.ataglance.walletglance.core.domain.result.error.AuthError
import com.ataglance.walletglance.core.domain.result.success.AuthSuccess

class FinishSignUpUseCaseImpl(
    private val authRepository: AuthRepository,
    private val userContext: UserContext
) : FinishSignUpUseCase {

    override suspend fun execute(
        oobCode: String
    ): Result<AuthSuccess, AuthError> {
        val result = authRepository.finishSignUp(oobCode = oobCode)

        result.getDataOrNull()?.let { user ->
            userContext.saveUserWithToken(user = user)
        }

        return result.toResult(success = AuthSuccess.SignedUp)
    }

}