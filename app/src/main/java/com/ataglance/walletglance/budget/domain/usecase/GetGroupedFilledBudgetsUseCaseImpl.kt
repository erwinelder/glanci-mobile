package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.data.repository.BudgetRepository
import com.ataglance.walletglance.budget.domain.model.GroupedBudgets
import com.ataglance.walletglance.budget.domain.model.GroupedFilledBudgets
import com.ataglance.walletglance.transaction.domain.usecase.GetTransactionsInDateRangeUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest

class GetGroupedFilledBudgetsUseCaseImpl(
    private val budgetRepository: BudgetRepository,
    private val getTransactionsInDateRangeUseCase: GetTransactionsInDateRangeUseCase
) : GetGroupedFilledBudgetsUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAsFlow(): Flow<GroupedFilledBudgets> {
        return budgetRepository.getAllBudgetsWithAccountIdsAsFlow().flatMapLatest { budgetsWithIds ->
            val groupedBudgets = GroupedBudgets.fromBudgets(budgets = budgetsWithIds)

            val dateRange = groupedBudgets.getMaxDateRange()
            getTransactionsInDateRangeUseCase.getAsFlowOrEmpty(range = dateRange).mapLatest { transactions ->
                GroupedFilledBudgets.fromBudgetsAndTransactions(
                    groupedBudgets = groupedBudgets, transactions = transactions
                )
            }
        }
    }

}