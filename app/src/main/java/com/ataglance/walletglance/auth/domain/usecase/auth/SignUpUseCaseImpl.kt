package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.auth.domain.repository.AuthRepository
import com.ataglance.walletglance.core.domain.result.Result
import com.ataglance.walletglance.core.domain.result.error.AuthError
import com.ataglance.walletglance.core.domain.result.success.AuthSuccess
import com.ataglance.walletglance.settings.domain.usecase.language.GetLanguagePreferenceUseCase

class SignUpUseCaseImpl(
    private val authRepository: AuthRepository,
    private val getLanguagePreferenceUseCase: GetLanguagePreferenceUseCase
) : SignUpUseCase {

    override suspend fun execute(
        name: String,
        email: String,
        password: String
    ): Result<AuthSuccess, AuthError> {
        val langCode = getLanguagePreferenceUseCase.get()

        val result =  authRepository.signUp(
            name = name, email = email, password = password, langCode = langCode
        )

        return result.toResult(success = AuthSuccess.SignUpEmailVerificationSent)
    }

}