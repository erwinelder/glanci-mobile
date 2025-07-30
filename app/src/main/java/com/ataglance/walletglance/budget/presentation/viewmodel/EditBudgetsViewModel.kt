package com.ataglance.walletglance.budget.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.budget.domain.usecase.GetBudgetsUseCase
import com.ataglance.walletglance.budget.domain.usecase.SaveBudgetsAndDeleteRestUseCase
import com.ataglance.walletglance.budget.mapper.budget.toBudgetWithIds
import com.ataglance.walletglance.budget.mapper.budget.toUiState
import com.ataglance.walletglance.budget.presentation.mapper.toUiState
import com.ataglance.walletglance.budget.presentation.model.BudgetDraft
import com.ataglance.walletglance.budget.presentation.model.GroupedBudgetsItemUiState
import com.ataglance.walletglance.budget.presentation.model.GroupedBudgetsUiState
import com.ataglance.walletglance.budget.presentation.utils.getBudgets
import com.ataglance.walletglance.budget.presentation.utils.getMaxIdOrZero
import com.ataglance.walletglance.category.domain.usecase.GetExpenseCategoriesGroupedUseCase
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.settings.domain.usecase.ChangeAppSetupStageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditBudgetsViewModel(
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getExpenseCategoriesGroupedUseCase: GetExpenseCategoriesGroupedUseCase,
    private val getBudgetsUseCase: GetBudgetsUseCase,
    private val saveBudgetsAndDeleteRestUseCase: SaveBudgetsAndDeleteRestUseCase,
    private val changeAppSetupStageUseCase: ChangeAppSetupStageUseCase
) : ViewModel() {

    private val _groupedBudgetsItems = MutableStateFlow<List<GroupedBudgetsItemUiState>>(emptyList())
    val groupedBudgetsItems = _groupedBudgetsItems.asStateFlow()

    fun applyBudget(budgetDraft: BudgetDraft) {
        val budgets = _groupedBudgetsItems.value.mapNotNull {
            (it as? GroupedBudgetsItemUiState.Budget)?.uiState
        }
        val budget = budgetDraft.toUiState()?.run {
            if (!budgetDraft.isNew) this else copy(id = budgets.getMaxIdOrZero() + 1)
        } ?: return

        val items = GroupedBudgetsUiState
            .fromBudgets(budgets = budgets)
            .run { if (budgetDraft.isNew) addBudget(budget) else replaceBudget(budget) }
            .asItems()

        _groupedBudgetsItems.update { items }
    }

    fun deleteBudget(id: Int, repeatingPeriod: RepeatingPeriod) {
        val budgets = _groupedBudgetsItems.value.getBudgets()

        val items = GroupedBudgetsUiState
            .fromBudgets(budgets = budgets)
            .deleteBudget(id = id, repeatingPeriod = repeatingPeriod)
            .asItems()

        _groupedBudgetsItems.update { items }
    }


    suspend fun saveBudgets() {
        val budgets = _groupedBudgetsItems.value.getBudgets().mapNotNull { it.toBudgetWithIds() }
        saveBudgetsAndDeleteRestUseCase.execute(budgets = budgets)
    }

    suspend fun preFinishSetup() {
        changeAppSetupStageUseCase.preFinishSetup()
    }


    init {
        viewModelScope.launch {
            val accounts = getAccountsUseCase.get()
            val categories = getExpenseCategoriesGroupedUseCase.execute()

            val items = getBudgetsUseCase.execute()
                .mapNotNull { it.toUiState(categories = categories, accounts = accounts) }
                .let { GroupedBudgetsUiState.fromBudgets(budgets = it) }
                .asItems()

            _groupedBudgetsItems.update { items }
        }
    }

}