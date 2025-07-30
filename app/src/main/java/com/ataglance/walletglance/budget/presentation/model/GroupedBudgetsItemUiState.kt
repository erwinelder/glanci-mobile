package com.ataglance.walletglance.budget.presentation.model

import androidx.annotation.StringRes

sealed class GroupedBudgetsItemUiState {

    data class RepeatingPeriodText(@StringRes val stringRes: Int) : GroupedBudgetsItemUiState()

    data class Budget(val uiState: BudgetUiState) : GroupedBudgetsItemUiState()

}
