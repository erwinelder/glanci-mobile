package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.data.repository.BudgetRepository
import com.ataglance.walletglance.budget.domain.model.BudgetWithIds
import com.ataglance.walletglance.core.utils.excludeItems

class SaveBudgetsAndDeleteRestUseCaseImpl(
    private val budgetRepository: BudgetRepository
) : SaveBudgetsAndDeleteRestUseCase {

    override suspend fun execute(budgets: List<BudgetWithIds>) {
        val currentBudgets = budgetRepository.getAllBudgets()

        val budgetsToDelete = currentBudgets.excludeItems(
            items = budgets,
            keySelector1 = { it.id },
            keySelector2 = { it.budgetId }
        )

        budgetRepository.deleteAndUpsertBudgetsWithAccountIds(
            toDelete = budgetsToDelete,
            toUpsert = budgets
        )
    }

}