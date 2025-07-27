package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.request.domain.model.result.Result
import com.ataglance.walletglance.request.domain.model.result.error.AuthError
import com.ataglance.walletglance.request.domain.model.result.success.AuthSuccess

interface UpdateNameUseCase {

    suspend fun execute(name: String): Result<AuthSuccess, AuthError>

}