package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.data.repository.BudgetOnWidgetRepository
import kotlinx.coroutines.flow.Flow

class GetBudgetIdsOnWidgetUseCaseImpl(
    private val budgetOnWidgetRepository: BudgetOnWidgetRepository
) : GetBudgetIdsOnWidgetUseCase {

    override fun getAsFlow(): Flow<List<Int>> {
        return budgetOnWidgetRepository.getAllBudgetsOnWidgetAsFlow()
    }

}