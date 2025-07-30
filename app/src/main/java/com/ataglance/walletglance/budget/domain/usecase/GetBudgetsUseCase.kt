package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.domain.model.BudgetWithIds

interface GetBudgetsUseCase {

    suspend fun execute(): List<BudgetWithIds>

}