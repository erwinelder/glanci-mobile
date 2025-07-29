package com.ataglance.walletglance.account.data.remote.source

import com.glanci.account.shared.dto.AccountCommandDto
import com.glanci.account.shared.dto.AccountQueryDto
import com.glanci.account.shared.service.AccountService
import com.glanci.request.shared.ResultData
import com.glanci.request.shared.SimpleResult
import com.glanci.request.shared.error.AccountDataError
import com.glanci.request.shared.error.DataError
import kotlinx.rpc.krpc.ktor.client.KtorRpcClient
import kotlinx.rpc.withService

class AccountRemoteDataSourceImpl(
    private val service: AccountService
) : AccountRemoteDataSource {

    constructor(client: KtorRpcClient) : this(service = client.withService<AccountService>())


    override suspend fun getUpdateTime(token: String): ResultData<Long, DataError> {
        return runCatching {
            service.getUpdateTime(token = token)
        }.getOrDefault(
            defaultValue = ResultData.Error(error = AccountDataError.AccountError)
        )
    }

    override suspend fun synchronizeAccounts(
        accounts: List<AccountCommandDto>,
        timestamp: Long,
        token: String
    ): SimpleResult<DataError> {
        return runCatching {
            service.saveAccounts(accounts = accounts, timestamp = timestamp, token = token)
        }.getOrDefault(
            defaultValue = SimpleResult.Error(error = AccountDataError.AccountError)
        )
    }

    override suspend fun getAccountsAfterTimestamp(
        timestamp: Long,
        token: String
    ): ResultData<List<AccountQueryDto>, DataError> {
        return runCatching {
            service.getAccountsAfterTimestamp(timestamp = timestamp, token = token)
        }.getOrDefault(
            defaultValue = ResultData.Error(error = AccountDataError.AccountError)
        )
    }

    override suspend fun synchronizeAccountsAndGetAfterTimestamp(
        accounts: List<AccountCommandDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): ResultData<List<AccountQueryDto>, DataError> {
        return runCatching {
            service.saveAccountsAndGetAfterTimestamp(
                accounts = accounts,
                timestamp = timestamp,
                localTimestamp = localTimestamp,
                token = token
            )
        }.getOrDefault(
            defaultValue = ResultData.Error(error = AccountDataError.AccountError)
        )
    }

}