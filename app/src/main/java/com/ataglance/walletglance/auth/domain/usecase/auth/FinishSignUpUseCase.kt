package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.request.domain.model.result.Result

interface FinishSignUpUseCase {
    suspend fun execute(oobCode: String): Result<AuthSuccess, AuthError>
}