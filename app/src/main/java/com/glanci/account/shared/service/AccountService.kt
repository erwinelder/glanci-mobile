package com.glanci.account.shared.service

import com.glanci.request.shared.ResultData
import com.glanci.request.shared.SimpleResult
import com.glanci.request.shared.error.DataError
import com.glanci.account.shared.dto.AccountCommandDto
import com.glanci.account.shared.dto.AccountQueryDto
import kotlinx.rpc.annotations.Rpc

@Rpc
interface AccountService {

    suspend fun getUpdateTime(token: String): ResultData<Long, DataError>

    suspend fun saveAccounts(
        accounts: List<AccountCommandDto>,
        timestamp: Long,
        token: String
    ): SimpleResult<DataError>

    suspend fun getAccountsAfterTimestamp(
        timestamp: Long,
        token: String
    ): ResultData<List<AccountQueryDto>, DataError>

    suspend fun saveAccountsAndGetAfterTimestamp(
        accounts: List<AccountCommandDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): ResultData<List<AccountQueryDto>, DataError>

}