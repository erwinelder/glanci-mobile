package com.ataglance.walletglance.budget.data.remote.source

import com.glanci.budget.shared.dto.BudgetWithAssociationsDto
import com.glanci.request.shared.ResultData
import com.glanci.request.shared.SimpleResult
import com.glanci.request.shared.error.DataError

interface BudgetRemoteDataSource {

    suspend fun getUpdateTime(token: String): ResultData<Long, DataError>

    suspend fun synchronizeBudgetsWithAssociations(
        budgets: List<BudgetWithAssociationsDto>,
        timestamp: Long,
        token: String
    ): SimpleResult<DataError>

    suspend fun getBudgetsWithAssociationsAfterTimestamp(
        timestamp: Long,
        token: String
    ): ResultData<List<BudgetWithAssociationsDto>, DataError>

    suspend fun synchronizeBudgetsWithAssociationsAndGetAfterTimestamp(
        budgets: List<BudgetWithAssociationsDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): ResultData<List<BudgetWithAssociationsDto>, DataError>

}