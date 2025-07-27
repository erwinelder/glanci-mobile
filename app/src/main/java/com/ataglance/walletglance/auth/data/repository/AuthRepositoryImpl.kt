package com.ataglance.walletglance.auth.data.repository

import com.ataglance.walletglance.auth.domain.model.CurrentAppVersion
import com.ataglance.walletglance.auth.domain.model.user.User
import com.ataglance.walletglance.auth.domain.model.user.UserContext
import com.ataglance.walletglance.auth.domain.model.user.UserWithToken
import com.ataglance.walletglance.auth.domain.repository.AuthRepository
import com.ataglance.walletglance.auth.mapper.toDomainError
import com.ataglance.walletglance.auth.mapper.toDomainModel
import com.ataglance.walletglance.auth.mapper.toDto
import com.ataglance.walletglance.request.domain.model.result.ResultData
import com.ataglance.walletglance.request.domain.model.result.SimpleResult
import com.ataglance.walletglance.request.domain.model.result.error.AuthError
import com.ataglance.walletglance.request.mapper.result.toDomainResultData
import com.ataglance.walletglance.request.mapper.result.toDomainSimpleResult
import com.glanci.auth.shared.service.AuthService
import kotlinx.rpc.krpc.ktor.client.KtorRpcClient
import kotlinx.rpc.withService

class AuthRepositoryImpl(
    private val service: AuthService,
    private val userContext: UserContext
) : AuthRepository {

    constructor(
        client: KtorRpcClient,
        userContext: UserContext
    ) : this(
        service = client.withService<AuthService>(),
        userContext = userContext
    )


    override suspend fun checkTokenValidity(
        appVersion: CurrentAppVersion,
        token: String
    ): ResultData<User, AuthError> {
        val appVersion = appVersion.toDto()

        val result = service.checkTokenValidity(appVersion = appVersion, token = token)

        return result.toDomainResultData(
            dataMapper = { it.toDomainModel() },
            errorMapper = { it.toDomainError() }
        )
    }

    override suspend fun signIn(
        email: String,
        password: String
    ): ResultData<UserWithToken, AuthError> {
        val result = service.signIn(email = email, password = password)

        return result.toDomainResultData(
            dataMapper = { it.toDomainModel() },
            errorMapper = { it.toDomainError() }
        )
    }

    override suspend fun signUp(
        name: String,
        email: String,
        password: String,
        langCode: String
    ): SimpleResult<AuthError> {
        val result = service.signUp(
            name = name, email = email, password = password, langCode = langCode
        )

        return result.toDomainSimpleResult { it.toDomainError() }
    }

    override suspend fun finishSignUp(oobCode: String): ResultData<UserWithToken, AuthError> {
        val result = service.finishSignUp(oobCode = oobCode)

        return result.toDomainResultData(
            dataMapper = { it.toDomainModel() },
            errorMapper = { it.toDomainError() }
        )
    }

    override suspend fun requestEmailUpdate(
        password: String,
        newEmail: String
    ): SimpleResult<AuthError> {
        val token = userContext.getAuthToken()
            ?: return SimpleResult.Error(AuthError.SessionExpired)

        val result = service.requestEmailUpdate(
            password = password, newEmail = newEmail, token = token
        )

        return result.toDomainSimpleResult { it.toDomainError() }
    }

    override suspend fun verifyEmailUpdate(oobCode: String): ResultData<UserWithToken, AuthError> {
        val token = userContext.getAuthToken()
            ?: return ResultData.Error(AuthError.SessionExpired)

        val result = service.verifyEmailUpdate(oobCode = oobCode, token = token)

        return result.toDomainResultData(
            dataMapper = { it.toDomainModel() },
            errorMapper = { it.toDomainError() }
        )
    }

    override suspend fun requestPasswordReset(email: String): SimpleResult<AuthError> {
        val result = service.requestPasswordReset(email = email)

        return result.toDomainSimpleResult { it.toDomainError() }
    }

    override suspend fun verifyPasswordReset(
        oobCode: String,
        newPassword: String
    ): SimpleResult<AuthError> {
        val result = service.verifyPasswordReset(oobCode = oobCode, newPassword = newPassword)

        return result.toDomainSimpleResult { it.toDomainError() }
    }

    override suspend fun updatePassword(
        password: String,
        newPassword: String
    ): SimpleResult<AuthError> {
        val token = userContext.getAuthToken()
            ?: return SimpleResult.Error(AuthError.SessionExpired)

        val result = service.updatePassword(
            password = password, newPassword = newPassword, token = token
        )

        return result.toDomainSimpleResult { it.toDomainError() }
    }

    override suspend fun saveUserName(name: String): SimpleResult<AuthError> {
        val token = userContext.getAuthToken()
            ?: return SimpleResult.Error(AuthError.SessionExpired)

        val result = service.saveUserName(name = name, token = token)

        return result.toDomainSimpleResult { it.toDomainError() }
    }

    override suspend fun saveUserLanguage(
        langCode: String,
        timestamp: Long
    ): SimpleResult<AuthError> {
        val token = userContext.getAuthToken()
            ?: return SimpleResult.Error(AuthError.SessionExpired)

        val result = service.saveUserLanguage(
            langCode = langCode, timestamp = timestamp, token = token
        )

        return result.toDomainSimpleResult { it.toDomainError() }
    }

    override suspend fun deleteAccount(
        email: String,
        password: String
    ): SimpleResult<AuthError> {
        val token = userContext.getAuthToken()
            ?: return SimpleResult.Error(AuthError.SessionExpired)

        val result = service.deleteAccount(email = email, password = password, token = token)

        return result.toDomainSimpleResult { it.toDomainError() }
    }

}