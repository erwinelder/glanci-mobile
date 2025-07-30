package com.ataglance.walletglance.budget.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.budget.domain.usecase.GetBudgetsOnWidgetUseCase
import com.ataglance.walletglance.budget.mapper.budget.toUiState
import com.ataglance.walletglance.budget.presentation.model.FilledBudgetUiState
import com.ataglance.walletglance.category.domain.usecase.GetExpenseCategoriesGroupedUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BudgetsOnWidgetViewModel(
    private val getBudgetsOnWidgetUseCase: GetBudgetsOnWidgetUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getExpenseCategoriesGroupedUseCase: GetExpenseCategoriesGroupedUseCase
) : ViewModel() {

    private val _budgets = MutableStateFlow<List<FilledBudgetUiState>>(emptyList())
    val budgets = _budgets.asStateFlow()


    init {
        viewModelScope.launch {
            val accounts = getAccountsUseCase.get()
            val categories = getExpenseCategoriesGroupedUseCase.execute()

            getBudgetsOnWidgetUseCase.getAsFlow().collectLatest { budgets ->
                val budgets = budgets.mapNotNull {
                    it.toUiState(categories = categories, accounts = accounts)
                }
                _budgets.update { budgets }
            }
        }
    }

}