package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.request.domain.model.result.SimpleResult
import com.ataglance.walletglance.request.domain.model.result.error.AuthError

interface CheckTokenValidityUseCase {

    suspend fun execute(): SimpleResult<AuthError>

}