package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.domain.repository.BudgetRepository
import com.ataglance.walletglance.budget.domain.model.FilledBudget
import com.ataglance.walletglance.budget.domain.utils.getMaxDateRange
import com.ataglance.walletglance.budget.domain.mapper.toFilledBudgetFiltered
import com.ataglance.walletglance.transaction.domain.usecase.GetTransactionsInDateRangeUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest

class GetBudgetsOnWidgetUseCaseImpl(
    private val budgetRepository: BudgetRepository,
    private val getBudgetIdsOnWidgetUseCase: GetBudgetIdsOnWidgetUseCase,
    private val getTransactionsInDateRangeUseCase: GetTransactionsInDateRangeUseCase
) : GetBudgetsOnWidgetUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAsFlow(): Flow<List<FilledBudget>> {
        return getBudgetIdsOnWidgetUseCase.getAsFlow().flatMapLatest { budgetIds ->
            budgetRepository.getBudgetWithAccountIdsByIdsAsFlow(budgetIds = budgetIds).flatMapLatest { budgetsWithIds ->
                val range = budgetsWithIds.getMaxDateRange()

                getTransactionsInDateRangeUseCase.getAsFlowOrEmpty(range = range).mapLatest { transactions ->
                    budgetsWithIds.map { it.toFilledBudgetFiltered(transactions = transactions) }
                }
            }
        }
    }

}