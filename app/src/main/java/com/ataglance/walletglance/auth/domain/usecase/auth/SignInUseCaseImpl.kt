package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.auth.domain.model.user.UserContext
import com.ataglance.walletglance.auth.domain.repository.AuthRepository
import com.ataglance.walletglance.core.domain.result.Result
import com.ataglance.walletglance.core.domain.result.error.AuthError
import com.ataglance.walletglance.core.domain.result.success.AuthSuccess
import com.ataglance.walletglance.settings.domain.usecase.language.SaveLanguageLocallyUseCase

class SignInUseCaseImpl(
    private val authRepository: AuthRepository,
    private val userContext: UserContext,
    private val saveLanguageLocallyUseCase: SaveLanguageLocallyUseCase
) : SignInUseCase {

    override suspend fun execute(email: String, password: String): Result<AuthSuccess, AuthError> {
        val result = authRepository.signIn(email = email, password = password)

        result.getDataOrNull()?.let { user ->
            userContext.saveUserWithToken(user = user)
            saveLanguageLocallyUseCase.execute(
                langCode = user.language.languageCode, timestamp = user.timestamp
            )
        }

        return result.toResult(success = AuthSuccess.SignedIn)
    }

}