package com.ataglance.walletglance.budget.presentation.utils

import com.ataglance.walletglance.budget.presentation.model.BudgetUiState
import com.ataglance.walletglance.budget.presentation.model.GroupedBudgetsItemUiState


fun List<BudgetUiState>.getMaxIdOrZero(): Int {
    return maxOfOrNull { it.id } ?: 0
}


fun List<GroupedBudgetsItemUiState>.getBudgets(): List<BudgetUiState> {
    return mapNotNull { (it as? GroupedBudgetsItemUiState.Budget)?.uiState }
}
