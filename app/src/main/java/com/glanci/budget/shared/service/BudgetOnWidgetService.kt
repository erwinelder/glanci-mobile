package com.glanci.budget.shared.service

import com.glanci.budget.shared.dto.BudgetOnWidgetDto
import com.glanci.request.shared.ResultData
import com.glanci.request.shared.SimpleResult
import com.glanci.request.shared.error.DataError
import kotlinx.rpc.annotations.Rpc

@Rpc
interface BudgetOnWidgetService {

    suspend fun getUpdateTime(token: String): ResultData<Long, DataError>

    suspend fun saveBudgetsOnWidget(
        budgets: List<BudgetOnWidgetDto>,
        timestamp: Long,
        token: String
    ): SimpleResult<DataError>

    suspend fun getBudgetsOnWidgetAfterTimestamp(
        timestamp: Long,
        token: String
    ): ResultData<List<BudgetOnWidgetDto>, DataError>

    suspend fun saveBudgetsOnWidgetAndGetAfterTimestamp(
        budgets: List<BudgetOnWidgetDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): ResultData<List<BudgetOnWidgetDto>, DataError>

}