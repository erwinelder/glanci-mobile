package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.auth.domain.model.CurrentAppVersion
import com.ataglance.walletglance.auth.domain.model.user.UserContext
import com.ataglance.walletglance.auth.domain.repository.AuthRepository
import com.ataglance.walletglance.auth.domain.usecase.user.GetUserProfileLocalTimestampUseCase
import com.ataglance.walletglance.core.data.local.preferences.SecureStorage
import com.ataglance.walletglance.request.domain.model.result.SimpleResult
import com.ataglance.walletglance.request.domain.model.result.error.AuthError
import com.ataglance.walletglance.settings.domain.usecase.language.GetLanguagePreferenceUseCase
import com.ataglance.walletglance.settings.domain.usecase.language.SaveLanguageLocallyUseCase
import com.ataglance.walletglance.settings.domain.usecase.language.SaveLanguageRemotelyUseCase

class CheckTokenValidityUseCaseImpl(
    private val secureStorage: SecureStorage,
    private val authRepository: AuthRepository,
    private val userContext: UserContext,
    private val getUserProfileLocalTimestampUseCase: GetUserProfileLocalTimestampUseCase,
    private val getLanguagePreferenceUseCase: GetLanguagePreferenceUseCase,
    private val saveLanguageLocallyUseCase: SaveLanguageLocallyUseCase,
    private val saveLanguagePreferenceRemotelyUseCase: SaveLanguageRemotelyUseCase
) : CheckTokenValidityUseCase {

    override suspend fun execute(): SimpleResult<AuthError> {
        val token = secureStorage.getAuthToken()
            ?: return SimpleResult.Error(AuthError.SessionExpired)
        val appVersion = CurrentAppVersion(5, 0, 0, alpha = 5)

        val result = authRepository.checkTokenValidity(appVersion = appVersion, token = token)

        result.getDataOrNull()?.let { user ->
            userContext.saveUser(user = user)
            syncDataIfRequired(
                remoteTimestamp = user.timestamp,
                remoteLangCode = user.language.languageCode
            )
        }

        return result.toSimpleResult()
    }

    private suspend fun syncDataIfRequired(
        remoteTimestamp: Long,
        remoteLangCode: String
    ) {
        val localTimestamp = getUserProfileLocalTimestampUseCase.get()
        if (remoteTimestamp == localTimestamp) return

        if (remoteTimestamp > localTimestamp) {
            saveLanguageLocallyUseCase.execute(
                langCode = remoteLangCode, timestamp = remoteTimestamp
            )
        } else {
            val localLangCode = getLanguagePreferenceUseCase.get()
            saveLanguagePreferenceRemotelyUseCase.execute(
                langCode = localLangCode, timestamp = localTimestamp
            )
        }
    }

}