package com.ataglance.walletglance.budget.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.budget.domain.usecase.GetGroupedFilledBudgetsUseCase
import com.ataglance.walletglance.budget.mapper.budget.toUiState
import com.ataglance.walletglance.budget.presentation.model.GroupedFilledBudgetsItemUiState
import com.ataglance.walletglance.category.domain.model.GroupedCategories
import com.ataglance.walletglance.category.domain.usecase.GetExpenseCategoriesGroupedUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BudgetsViewModel(
    private val getGroupedFilledBudgetsUseCase: GetGroupedFilledBudgetsUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getExpenseCategoriesGroupedUseCase: GetExpenseCategoriesGroupedUseCase,
) : ViewModel() {

    private var accounts = emptyList<Account>()
    private var categories = emptyList<GroupedCategories>()


    private val _groupedBudgetsItems = MutableStateFlow<List<GroupedFilledBudgetsItemUiState>>(emptyList())
    val groupedBudgetsItems = _groupedBudgetsItems.asStateFlow()


    init {
        viewModelScope.launch {
            accounts = getAccountsUseCase.get()
            categories = getExpenseCategoriesGroupedUseCase.execute()

            getGroupedFilledBudgetsUseCase.getAsFlow().collectLatest { groupedBudgets ->
                val items = groupedBudgets
                    .toUiState(categories = categories, accounts = accounts)
                    .asItems()

                _groupedBudgetsItems.update { items }
            }
        }
    }

}