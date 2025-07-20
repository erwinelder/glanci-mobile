package com.glanci.budget.shared.service

import com.glanci.budget.shared.dto.BudgetOnWidgetDto
import kotlinx.rpc.annotations.Rpc

@Rpc
interface BudgetOnWidgetService {

    suspend fun getUpdateTime(token: String): Long?

    suspend fun saveBudgetsOnWidget(budgets: List<BudgetOnWidgetDto>, timestamp: Long, token: String)

    suspend fun getBudgetsOnWidgetAfterTimestamp(timestamp: Long, token: String): List<BudgetOnWidgetDto>?

    suspend fun saveBudgetsOnWidgetAndGetAfterTimestamp(
        budgets: List<BudgetOnWidgetDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): List<BudgetOnWidgetDto>?

}