package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.request.domain.model.result.Result
import com.ataglance.walletglance.request.domain.model.result.error.AuthError
import com.ataglance.walletglance.request.domain.model.result.success.AuthSuccess

interface RequestEmailUpdateUseCase {

    suspend fun execute(password: String, newEmail: String): Result<AuthSuccess, AuthError>

}