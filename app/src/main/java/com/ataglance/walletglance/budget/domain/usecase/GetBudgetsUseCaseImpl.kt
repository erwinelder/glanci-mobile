package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.domain.repository.BudgetRepository
import com.ataglance.walletglance.budget.domain.model.BudgetWithIds

class GetBudgetsUseCaseImpl(
    private val budgetsRepository: BudgetRepository
) : GetBudgetsUseCase {

    override suspend fun execute(): List<BudgetWithIds> {
        return budgetsRepository.getAllBudgetsWithAccountIds()
    }

}