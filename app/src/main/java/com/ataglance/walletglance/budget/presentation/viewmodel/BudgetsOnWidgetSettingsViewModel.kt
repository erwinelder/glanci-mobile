package com.ataglance.walletglance.budget.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.budget.domain.usecase.GetBudgetIdsOnWidgetUseCase
import com.ataglance.walletglance.budget.domain.usecase.GetGroupedBudgetsUseCase
import com.ataglance.walletglance.budget.domain.usecase.SaveBudgetsOnWidgetAndDeleteRestUseCase
import com.ataglance.walletglance.budget.mapper.toUiState
import com.ataglance.walletglance.budget.presentation.model.GroupedBudgetsUiState
import com.ataglance.walletglance.category.domain.model.GroupedCategories
import com.ataglance.walletglance.category.domain.usecase.GetExpenseCategoriesGroupedUseCase
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BudgetsOnWidgetSettingsViewModel(
    private val saveBudgetsOnWidgetAndDeleteRestUseCase: SaveBudgetsOnWidgetAndDeleteRestUseCase,
    private val getBudgetIdsOnWidgetUseCase: GetBudgetIdsOnWidgetUseCase,
    private val getGroupedBudgetsUseCase: GetGroupedBudgetsUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getExpenseCategoriesGroupedUseCase: GetExpenseCategoriesGroupedUseCase,
) : ViewModel() {

    private var accounts = emptyList<Account>()
    private var categories = emptyList<GroupedCategories>()


    private val _openedWidgetSettings: MutableStateFlow<WidgetName?> = MutableStateFlow(null)
    val openedWidgetSettings = _openedWidgetSettings.asStateFlow()

    fun openWidgetSettings(widgetName: WidgetName) {
        _openedWidgetSettings.update { widgetName }
    }

    fun closeWidgetSettings() {
        _openedWidgetSettings.update { null }
    }


    private val _groupedBudgets = MutableStateFlow(GroupedBudgetsUiState())

    private val _checkedBudgetIds = MutableStateFlow<List<Int>>(emptyList())

    fun checkBudgetOnWidget(budgetId: Int) {
        _checkedBudgetIds.update {
            it.toMutableList().apply { add(budgetId) }
        }
    }

    fun uncheckBudgetOnWidget(budgetId: Int) {
        _checkedBudgetIds.update {
            it.toMutableList().apply { remove(budgetId) }
        }
    }


    val groupedBudgetsItems = combine(
        _groupedBudgets,
        _checkedBudgetIds
    ) { groupedBudgets, checkedBudgetIds ->
        groupedBudgets.asCheckedItems(checkedBudgetIds = checkedBudgetIds)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    @OptIn(ExperimentalCoroutinesApi::class)
    val checkedBudgetsLimitIsReached = _checkedBudgetIds.mapLatest { budgetIds ->
        budgetIds.size >= 3
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )


    fun saveCurrentBudgetsOnWidget() {
        viewModelScope.launch {
            saveBudgetsOnWidgetAndDeleteRestUseCase.execute(budgetIds = _checkedBudgetIds.value)
        }
    }


    init {
        viewModelScope.launch {
            accounts = getAccountsUseCase.get()
            categories = getExpenseCategoriesGroupedUseCase.execute()

            getGroupedBudgetsUseCase.getAsFlow().collectLatest { groupedBudgets ->
                _groupedBudgets.update {
                    groupedBudgets.toUiState(categories = categories, accounts = accounts)
                }
            }
        }
        viewModelScope.launch {
            getBudgetIdsOnWidgetUseCase.getAsFlow().collectLatest { budgetsIds ->
                _checkedBudgetIds.update { budgetsIds }
            }
        }
    }

}