package com.ataglance.walletglance.budget.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.budget.domain.usecase.GetBudgetUseCase
import com.ataglance.walletglance.budget.presentation.model.BudgetStatisticsScreenUiState
import com.ataglance.walletglance.category.domain.usecase.GetExpenseCategoriesGroupedUseCase
import com.ataglance.walletglance.category.domain.utils.getCategoryWithSubcategoryById
import com.ataglance.walletglance.core.domain.statistics.ColumnChartUiState
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import com.ataglance.walletglance.core.utils.getPrevDateRanges
import com.ataglance.walletglance.transaction.domain.usecase.GetTotalExpensesInDateRangesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BudgetStatisticsViewModel(
    budgetId: Int,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getExpenseCategoriesGroupedUseCase: GetExpenseCategoriesGroupedUseCase,
    private val getBudgetUseCase: GetBudgetUseCase,
    private val getTotalExpensesInDateRangesUseCase: GetTotalExpensesInDateRangesUseCase,
    private val resourceManager: ResourceManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetStatisticsScreenUiState())
    val uiState = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            val budget = getBudgetUseCase.execute(id = budgetId) ?: return@launch
            val accounts = getAccountsUseCase.get().filter { it.id in budget.accountIds }
            val category = getExpenseCategoriesGroupedUseCase.execute()
                .getCategoryWithSubcategoryById(id = budget.categoryId)
                ?.getSubcategoryOrCategory()

            getTotalExpensesInDateRangesUseCase
                .getByCategoryAndAccounts(
                    categoryId = budget.categoryId,
                    accountIds = budget.accountIds,
                    dateRanges = budget.repeatingPeriod.getPrevDateRanges()
                )
                .collectLatest { totalInRanges ->
                    _uiState.update {
                        BudgetStatisticsScreenUiState(
                            id = budget.budgetId,
                            amountLimit = budget.amountLimit,
                            category = category,
                            budgetName = budget.name,
                            repeatingPeriod = budget.repeatingPeriod,
                            currency = accounts.firstOrNull()?.currency ?: "",
                            accounts = accounts,
                            columnChartUiState = ColumnChartUiState.asAmountsByDateRanges(
                                totalAmountsByRanges = totalInRanges,
                                rowsCount = 5,
                                repeatingPeriod = budget.repeatingPeriod,
                                resourceManager = resourceManager
                            )
                        )
                    }
                }
        }
    }

}