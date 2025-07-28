package com.ataglance.walletglance.auth.domain.repository

import com.ataglance.walletglance.auth.domain.model.CurrentAppVersion
import com.ataglance.walletglance.auth.domain.model.user.User
import com.ataglance.walletglance.auth.domain.model.user.UserWithToken
import com.ataglance.walletglance.request.domain.model.result.ResultData
import com.ataglance.walletglance.request.domain.model.result.SimpleResult
import com.ataglance.walletglance.request.domain.model.result.error.AuthError

interface AuthRepository {

    suspend fun checkTokenValidity(
        appVersion: CurrentAppVersion,
        token: String
    ): ResultData<User, AuthError>

    suspend fun signIn(email: String, password: String): ResultData<UserWithToken, AuthError>

    suspend fun signUp(
        name: String,
        email: String,
        password: String,
        langCode: String
    ): SimpleResult<AuthError>

    suspend fun finishSignUp(oobCode: String): ResultData<UserWithToken, AuthError>

    suspend fun requestEmailUpdate(password: String, newEmail: String): SimpleResult<AuthError>

    suspend fun verifyEmailUpdate(oobCode: String): ResultData<UserWithToken, AuthError>

    suspend fun finishEmailUpdate(newEmail: String, password: String): ResultData<UserWithToken, AuthError>

    suspend fun requestPasswordReset(email: String): SimpleResult<AuthError>

    suspend fun verifyPasswordReset(oobCode: String, newPassword: String): SimpleResult<AuthError>

    suspend fun updatePassword(password: String, newPassword: String): SimpleResult<AuthError>

    suspend fun saveUserName(name: String): SimpleResult<AuthError>

    suspend fun saveUserLanguage(langCode: String, timestamp: Long): SimpleResult<AuthError>

    suspend fun deleteAccount(email: String, password: String): SimpleResult<AuthError>

}