package com.ataglance.walletglance.budget.data.remote.source

import com.ataglance.walletglance.budget.data.remote.model.BudgetOnWidgetDto

class BudgetOnWidgetRemoteDataSourceImpl() : BudgetOnWidgetRemoteDataSource {

    override suspend fun getUpdateTime(token: String): Long? {
        // TODO("Not yet implemented")
        return null
    }

    override suspend fun synchronizeBudgetsOnWidget(
        budgets: List<BudgetOnWidgetDto>,
        timestamp: Long,
        token: String
    ): Boolean {
        // TODO("Not yet implemented")
        return false
    }

    override suspend fun synchronizeBudgetsOnWidgetAndGetAfterTimestamp(
        budgets: List<BudgetOnWidgetDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): List<BudgetOnWidgetDto>? {
        // TODO("Not yet implemented")
        return null
    }

    override suspend fun getBudgetsOnWidgetAfterTimestamp(
        timestamp: Long,
        token: String
    ): List<BudgetOnWidgetDto>? {
        // TODO("Not yet implemented")
        return null
    }

}