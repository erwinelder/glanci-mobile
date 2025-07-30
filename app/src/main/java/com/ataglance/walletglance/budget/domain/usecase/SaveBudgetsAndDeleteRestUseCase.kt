package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.domain.model.BudgetWithIds

interface SaveBudgetsAndDeleteRestUseCase {

    suspend fun execute(budgets: List<BudgetWithIds>)

}