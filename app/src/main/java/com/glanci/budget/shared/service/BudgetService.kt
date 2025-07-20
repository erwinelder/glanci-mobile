package com.glanci.budget.shared.service

import com.glanci.budget.shared.dto.BudgetWithAssociationsDto
import kotlinx.rpc.annotations.Rpc

@Rpc
interface BudgetService {

    suspend fun getUpdateTime(token: String): Long?

    suspend fun saveBudgetsWithAssociations(budgets: List<BudgetWithAssociationsDto>, timestamp: Long, token: String)

    suspend fun getBudgetsWithAssociationsAfterTimestamp(
        timestamp: Long,
        token: String
    ): List<BudgetWithAssociationsDto>?

    suspend fun saveBudgetsWithAssociationsAndGetAfterTimestamp(
        budgets: List<BudgetWithAssociationsDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): List<BudgetWithAssociationsDto>?

}