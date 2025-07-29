package com.ataglance.walletglance.settings.domain.usecase.language

import com.ataglance.walletglance.core.domain.result.SimpleResult
import com.ataglance.walletglance.core.domain.result.error.AuthError

interface SaveLanguageRemotelyUseCase {

    suspend fun execute(langCode: String, timestamp: Long): SimpleResult<AuthError>

}