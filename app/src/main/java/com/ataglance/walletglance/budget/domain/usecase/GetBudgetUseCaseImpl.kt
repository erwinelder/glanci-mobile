package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.data.repository.BudgetRepository
import com.ataglance.walletglance.budget.domain.model.BudgetWithIds

class GetBudgetUseCaseImpl(
    private val budgetRepository: BudgetRepository
) : GetBudgetUseCase {

    override suspend fun execute(id: Int): BudgetWithIds? {
        return budgetRepository.getBudgetWithAccountIds(budgetId = id)
    }

}