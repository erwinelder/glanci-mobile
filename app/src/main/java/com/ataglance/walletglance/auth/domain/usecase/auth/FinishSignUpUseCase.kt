package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.core.domain.result.Result
import com.ataglance.walletglance.core.domain.result.error.AuthError
import com.ataglance.walletglance.core.domain.result.success.AuthSuccess

interface FinishSignUpUseCase {

    suspend fun execute(oobCode: String): Result<AuthSuccess, AuthError>

}