package com.ataglance.walletglance.budget.data.repository

import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.BudgetWithIds
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {

    suspend fun deleteAndUpsertBudgetsWithAccountIds(
        toDelete: List<Budget>,
        toUpsert: List<BudgetWithIds>
    )

    suspend fun getBudgetWithAccountIds(budgetId: Int): BudgetWithIds?

    fun getBudgetWithAccountIdsByIdsAsFlow(budgetIds: List<Int>): Flow<List<BudgetWithIds>>

    suspend fun getAllBudgets(): List<Budget>

    fun getAllBudgetsWithAccountIdsAsFlow(): Flow<List<BudgetWithIds>>

    suspend fun getAllBudgetsWithAccountIds(): List<BudgetWithIds>

}