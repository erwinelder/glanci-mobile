package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.data.repository.BudgetRepository
import com.ataglance.walletglance.budget.domain.model.GroupedBudgets
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class GetGroupedBudgetsUseCaseImpl(
    private val budgetRepository: BudgetRepository
) : GetGroupedBudgetsUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAsFlow(): Flow<GroupedBudgets> {
        return budgetRepository.getAllBudgetsWithAccountIdsAsFlow().mapLatest { budgetsWithIds ->
            GroupedBudgets.fromBudgets(budgets = budgetsWithIds)
        }
    }

}