package com.ataglance.walletglance.account.data.remote.source

import com.glanci.account.shared.dto.AccountCommandDto
import com.glanci.account.shared.dto.AccountQueryDto

interface AccountRemoteDataSource {

    suspend fun getUpdateTime(token: String): Long?

    suspend fun synchronizeAccounts(
        accounts: List<AccountCommandDto>,
        timestamp: Long,
        token: String
    ): Boolean

    suspend fun getAccountsAfterTimestamp(timestamp: Long, token: String): List<AccountQueryDto>?

    suspend fun synchronizeAccountsAndGetAfterTimestamp(
        accounts: List<AccountCommandDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): List<AccountQueryDto>?

}