package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.domain.model.BudgetWithIds

interface GetBudgetUseCase {

    suspend fun execute(id: Int): BudgetWithIds?

}