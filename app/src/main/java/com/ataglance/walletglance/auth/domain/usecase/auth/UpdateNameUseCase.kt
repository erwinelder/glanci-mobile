package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.core.domain.result.Result
import com.ataglance.walletglance.core.domain.result.error.AuthError
import com.ataglance.walletglance.core.domain.result.success.AuthSuccess

interface UpdateNameUseCase {

    suspend fun execute(name: String): Result<AuthSuccess, AuthError>

}