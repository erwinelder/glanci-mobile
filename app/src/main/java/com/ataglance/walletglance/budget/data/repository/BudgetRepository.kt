package com.ataglance.walletglance.budget.data.repository

import com.ataglance.walletglance.budget.data.model.BudgetDataModel
import com.ataglance.walletglance.budget.data.model.BudgetWithAssociationsDataModel

interface BudgetRepository {

    suspend fun deleteAndUpsertBudgetsWithAssociations(
        toDelete: List<BudgetDataModel>,
        toUpsert: List<BudgetWithAssociationsDataModel>
    )

    suspend fun getAllBudgets(): List<BudgetDataModel>

    suspend fun getBudgetWithAssociations(budgetId: Int): BudgetWithAssociationsDataModel?

    suspend fun getAllBudgetsWithAssociations(): List<BudgetWithAssociationsDataModel>

}