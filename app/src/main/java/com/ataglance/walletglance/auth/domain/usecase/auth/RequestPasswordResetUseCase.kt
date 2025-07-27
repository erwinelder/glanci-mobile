package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.request.domain.model.result.Result
import com.ataglance.walletglance.request.domain.model.result.error.AuthError
import com.ataglance.walletglance.request.domain.model.result.success.AuthSuccess

interface RequestPasswordResetUseCase {

    suspend fun execute(email: String): Result<AuthSuccess, AuthError>

}