package com.ataglance.walletglance.budget.data.remote.source

import com.ataglance.walletglance.budget.data.remote.model.BudgetOnWidgetDto

interface BudgetOnWidgetRemoteDataSource {

    suspend fun getUpdateTime(token: String): Long?

    suspend fun synchronizeBudgetsOnWidget(
        budgets: List<BudgetOnWidgetDto>,
        timestamp: Long,
        token: String
    ): Boolean

    suspend fun synchronizeBudgetsOnWidgetAndGetAfterTimestamp(
        budgets: List<BudgetOnWidgetDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): List<BudgetOnWidgetDto>?

    suspend fun getBudgetsOnWidgetAfterTimestamp(
        timestamp: Long,
        token: String
    ): List<BudgetOnWidgetDto>?

}