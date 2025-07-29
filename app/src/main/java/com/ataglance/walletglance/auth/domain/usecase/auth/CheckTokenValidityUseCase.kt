package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.core.domain.result.SimpleResult
import com.ataglance.walletglance.core.domain.result.error.AuthError

interface CheckTokenValidityUseCase {

    suspend fun execute(): SimpleResult<AuthError>

}