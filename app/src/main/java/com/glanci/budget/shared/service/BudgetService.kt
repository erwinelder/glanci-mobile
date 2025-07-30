package com.glanci.budget.shared.service

import com.glanci.budget.shared.dto.BudgetWithAssociationsDto
import com.glanci.request.shared.ResultData
import com.glanci.request.shared.SimpleResult
import com.glanci.request.shared.error.DataError
import kotlinx.rpc.annotations.Rpc

@Rpc
interface BudgetService {

    suspend fun getUpdateTime(token: String): ResultData<Long, DataError>

    suspend fun saveBudgetsWithAssociations(
        budgets: List<BudgetWithAssociationsDto>,
        timestamp: Long,
        token: String
    ): SimpleResult<DataError>

    suspend fun getBudgetsWithAssociationsAfterTimestamp(
        timestamp: Long,
        token: String
    ): ResultData<List<BudgetWithAssociationsDto>, DataError>

    suspend fun saveBudgetsWithAssociationsAndGetAfterTimestamp(
        budgets: List<BudgetWithAssociationsDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): ResultData<List<BudgetWithAssociationsDto>, DataError>

}