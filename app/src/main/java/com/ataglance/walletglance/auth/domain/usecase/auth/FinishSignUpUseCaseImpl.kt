package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.auth.data.repository.AuthRepository
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.auth.domain.model.user.UserContext
import com.ataglance.walletglance.auth.mapper.toDomainModel
import com.ataglance.walletglance.request.domain.model.result.Result

class FinishSignUpUseCaseImpl(
    private val authRepository: AuthRepository,
    private val userContext: UserContext
) : FinishSignUpUseCase {
    override suspend fun execute(
        oobCode: String
    ): Result<AuthSuccess, AuthError> {
        val result = authRepository.finishSignUpWithEmailAndPassword(oobCode = oobCode)

        result.getDataIfSuccess()?.let { data ->
            val user = data.toDomainModel() ?: return Result.Error(AuthError.RequestDataNotValid)
            userContext.saveUserWithToken(user = user)
        }

        return result.toDefaultResult(success = AuthSuccess.SignedUp)
    }
}