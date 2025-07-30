package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.data.repository.BudgetOnWidgetRepository
import com.ataglance.walletglance.core.utils.excludeItems

class SaveBudgetsOnWidgetAndDeleteRestUseCaseImpl(
    private val budgetOnWidgetRepository: BudgetOnWidgetRepository
) : SaveBudgetsOnWidgetAndDeleteRestUseCase {

    override suspend fun execute(budgetIds: List<Int>) {
        val currBudgetIds = budgetOnWidgetRepository.getAllBudgetsOnWidget()

        val budgetsToDelete = currBudgetIds.excludeItems(budgetIds) { it }
        val budgetsToUpsert = budgetIds.excludeItems(currBudgetIds) { it }

        budgetOnWidgetRepository.deleteAndUpsertBudgetsOnWidget(
            toDelete = budgetsToDelete, toUpsert = budgetsToUpsert
        )
    }

}