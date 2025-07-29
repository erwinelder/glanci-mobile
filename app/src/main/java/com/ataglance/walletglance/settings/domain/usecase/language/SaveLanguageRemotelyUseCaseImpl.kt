package com.ataglance.walletglance.settings.domain.usecase.language

import com.ataglance.walletglance.auth.domain.repository.AuthRepository
import com.ataglance.walletglance.core.domain.result.SimpleResult
import com.ataglance.walletglance.core.domain.result.error.AuthError

class SaveLanguageRemotelyUseCaseImpl(
    private val authRepository: AuthRepository
) : SaveLanguageRemotelyUseCase {

    override suspend fun execute(langCode: String, timestamp: Long): SimpleResult<AuthError> {
        return authRepository.saveUserLanguage(langCode = langCode, timestamp = timestamp)
    }

}