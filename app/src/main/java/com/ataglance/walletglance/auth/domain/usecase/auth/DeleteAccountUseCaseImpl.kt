package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.auth.domain.model.user.UserContext
import com.ataglance.walletglance.auth.domain.repository.AuthRepository
import com.ataglance.walletglance.core.domain.usecase.DeleteAllDataLocallyUseCase
import com.ataglance.walletglance.core.domain.result.Result
import com.ataglance.walletglance.core.domain.result.SimpleResult
import com.ataglance.walletglance.core.domain.result.error.AuthError
import com.ataglance.walletglance.core.domain.result.success.AuthSuccess

class DeleteAccountUseCaseImpl(
    private val authRepository: AuthRepository,
    private val userContext: UserContext,
    private val deleteAllDataLocallyUseCase: DeleteAllDataLocallyUseCase
) : DeleteAccountUseCase {

    override suspend fun execute(password: String): Result<AuthSuccess, AuthError> {
        val email = userContext.email ?: return Result.Error(AuthError.SessionExpired)

        val result = authRepository.deleteAccount(email = email, password = password)

        if (result is SimpleResult.Success) {
            userContext.deleteData()
            deleteAllDataLocallyUseCase.execute()
        }

        return result.toResult(success = AuthSuccess.AccountDeleted)
    }

}