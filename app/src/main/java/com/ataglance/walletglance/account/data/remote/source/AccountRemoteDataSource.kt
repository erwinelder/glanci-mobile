package com.ataglance.walletglance.account.data.remote.source

import com.glanci.account.shared.dto.AccountCommandDto
import com.glanci.account.shared.dto.AccountQueryDto
import com.glanci.request.shared.ResultData
import com.glanci.request.shared.SimpleResult
import com.glanci.request.shared.error.DataError

interface AccountRemoteDataSource {

    suspend fun getUpdateTime(token: String): ResultData<Long, DataError>

    suspend fun synchronizeAccounts(
        accounts: List<AccountCommandDto>,
        timestamp: Long,
        token: String
    ): SimpleResult<DataError>

    suspend fun getAccountsAfterTimestamp(
        timestamp: Long,
        token: String
    ): ResultData<List<AccountQueryDto>, DataError>

    suspend fun synchronizeAccountsAndGetAfterTimestamp(
        accounts: List<AccountCommandDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): ResultData<List<AccountQueryDto>, DataError>

}