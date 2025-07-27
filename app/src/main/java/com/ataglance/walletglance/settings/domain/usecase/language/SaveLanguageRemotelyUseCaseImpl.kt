package com.ataglance.walletglance.settings.domain.usecase.language

import com.ataglance.walletglance.auth.domain.repository.AuthRepository
import com.ataglance.walletglance.request.domain.model.result.SimpleResult
import com.ataglance.walletglance.request.domain.model.result.error.AuthError

class SaveLanguageRemotelyUseCaseImpl(
    private val authRepository: AuthRepository
) : SaveLanguageRemotelyUseCase {

    override suspend fun execute(langCode: String, timestamp: Long): SimpleResult<AuthError> {
        return authRepository.saveUserLanguage(langCode = langCode, timestamp = timestamp)
    }

}