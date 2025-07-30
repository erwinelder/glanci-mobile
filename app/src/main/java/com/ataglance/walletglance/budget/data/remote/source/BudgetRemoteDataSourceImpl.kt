package com.ataglance.walletglance.budget.data.remote.source

import com.glanci.budget.shared.dto.BudgetWithAssociationsDto
import com.glanci.budget.shared.service.BudgetService
import com.glanci.request.shared.ResultData
import com.glanci.request.shared.SimpleResult
import com.glanci.request.shared.error.BudgetDataError
import com.glanci.request.shared.error.DataError
import kotlinx.rpc.krpc.ktor.client.KtorRpcClient
import kotlinx.rpc.withService

class BudgetRemoteDataSourceImpl(
    private val service: BudgetService
) : BudgetRemoteDataSource {

    constructor(client: KtorRpcClient) : this(service = client.withService<BudgetService>())


    override suspend fun getUpdateTime(token: String): ResultData<Long, DataError> {
        return runCatching {
            service.getUpdateTime(token = token)
        }.getOrDefault(
            defaultValue = ResultData.Error(error = BudgetDataError.BudgetError)
        )
    }

    override suspend fun synchronizeBudgetsWithAssociations(
        budgets: List<BudgetWithAssociationsDto>,
        timestamp: Long,
        token: String
    ): SimpleResult<DataError> {
        return runCatching {
            service.saveBudgetsWithAssociations(
                budgets = budgets,
                timestamp = timestamp,
                token = token
            )
        }.getOrDefault(
            defaultValue = SimpleResult.Error(error = BudgetDataError.BudgetError)
        )
    }

    override suspend fun getBudgetsWithAssociationsAfterTimestamp(
        timestamp: Long,
        token: String
    ): ResultData<List<BudgetWithAssociationsDto>, DataError> {
        return runCatching {
            service.getBudgetsWithAssociationsAfterTimestamp(timestamp = timestamp, token = token)
        }.getOrDefault(
            defaultValue = ResultData.Error(error = BudgetDataError.BudgetError)
        )
    }

    override suspend fun synchronizeBudgetsWithAssociationsAndGetAfterTimestamp(
        budgets: List<BudgetWithAssociationsDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): ResultData<List<BudgetWithAssociationsDto>, DataError> {
        return runCatching {
            service.saveBudgetsWithAssociationsAndGetAfterTimestamp(
                budgets = budgets,
                timestamp = timestamp,
                localTimestamp = localTimestamp,
                token = token
            )
        }.getOrDefault(
            defaultValue = ResultData.Error(error = BudgetDataError.BudgetError)
        )
    }

}