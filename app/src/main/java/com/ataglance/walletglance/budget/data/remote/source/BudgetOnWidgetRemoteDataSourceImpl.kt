package com.ataglance.walletglance.budget.data.remote.source

import com.glanci.budget.shared.dto.BudgetOnWidgetDto
import com.glanci.budget.shared.service.BudgetOnWidgetService
import com.glanci.request.shared.ResultData
import com.glanci.request.shared.SimpleResult
import com.glanci.request.shared.error.BudgetOnWidgetDataError
import com.glanci.request.shared.error.DataError
import kotlinx.rpc.krpc.ktor.client.KtorRpcClient
import kotlinx.rpc.withService

class BudgetOnWidgetRemoteDataSourceImpl(
    private val service: BudgetOnWidgetService
) : BudgetOnWidgetRemoteDataSource {

    constructor(client: KtorRpcClient) : this(service = client.withService<BudgetOnWidgetService>())


    override suspend fun getUpdateTime(token: String): ResultData<Long, DataError> {
        return runCatching {
            service.getUpdateTime(token = token)
        }.getOrDefault(
            defaultValue = ResultData.Error(error = BudgetOnWidgetDataError.BudgetsOnWidgetError)
        )
    }

    override suspend fun synchronizeBudgetsOnWidget(
        budgets: List<BudgetOnWidgetDto>,
        timestamp: Long,
        token: String
    ): SimpleResult<DataError> {
        return runCatching {
            service.saveBudgetsOnWidget(budgets = budgets, timestamp = timestamp, token = token)
        }.getOrDefault(
            defaultValue = SimpleResult.Error(error = BudgetOnWidgetDataError.BudgetsOnWidgetError)
        )
    }

    override suspend fun getBudgetsOnWidgetAfterTimestamp(
        timestamp: Long,
        token: String
    ): ResultData<List<BudgetOnWidgetDto>, DataError> {
        return runCatching {
            service.getBudgetsOnWidgetAfterTimestamp(timestamp = timestamp, token = token)
        }.getOrDefault(
            defaultValue = ResultData.Error(error = BudgetOnWidgetDataError.BudgetsOnWidgetError)
        )
    }

    override suspend fun synchronizeBudgetsOnWidgetAndGetAfterTimestamp(
        budgets: List<BudgetOnWidgetDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): ResultData<List<BudgetOnWidgetDto>, DataError> {
        return runCatching {
            service.saveBudgetsOnWidgetAndGetAfterTimestamp(
                budgets = budgets,
                timestamp = timestamp,
                localTimestamp = localTimestamp,
                token = token
            )
        }.getOrDefault(
            defaultValue = ResultData.Error(error = BudgetOnWidgetDataError.BudgetsOnWidgetError)
        )
    }

}