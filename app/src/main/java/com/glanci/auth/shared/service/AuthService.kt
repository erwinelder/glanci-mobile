package com.glanci.auth.shared.service

import com.glanci.auth.shared.dto.CheckAppVersionRequestDto
import com.glanci.auth.shared.dto.UserDto
import com.glanci.auth.shared.dto.UserWithTokenDto
import com.glanci.request.shared.ResultData
import com.glanci.request.shared.SimpleResult
import com.glanci.request.shared.error.AuthDataError
import kotlinx.rpc.annotations.Rpc

@Rpc
interface AuthService {

    suspend fun checkTokenValidity(appVersion: CheckAppVersionRequestDto, token: String): ResultData<UserDto, AuthDataError>

    suspend fun signIn(email: String, password: String): ResultData<UserWithTokenDto, AuthDataError>

    suspend fun signUp(name: String, email: String, password: String, langCode: String): SimpleResult<AuthDataError>

    suspend fun finishSignUp(oobCode: String): ResultData<UserWithTokenDto, AuthDataError>

    suspend fun requestEmailUpdate(password: String, newEmail: String, token: String): SimpleResult<AuthDataError>

    suspend fun verifyEmailUpdate(oobCode: String, token: String): ResultData<UserWithTokenDto, AuthDataError>

    suspend fun finishEmailUpdate(newEmail: String, password: String, token: String): ResultData<UserWithTokenDto, AuthDataError>

    suspend fun requestPasswordReset(email: String): SimpleResult<AuthDataError>

    suspend fun verifyPasswordReset(oobCode: String, newPassword: String): SimpleResult<AuthDataError>

    suspend fun updatePassword(password: String, newPassword: String, token: String): SimpleResult<AuthDataError>

    suspend fun saveUserName(name: String, token: String): SimpleResult<AuthDataError>

    suspend fun saveUserLanguage(langCode: String, timestamp: Long, token: String): SimpleResult<AuthDataError>

    suspend fun deleteAccount(email: String, password: String, token: String): SimpleResult<AuthDataError>

}
