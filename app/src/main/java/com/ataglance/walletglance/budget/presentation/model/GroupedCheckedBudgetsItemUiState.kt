package com.ataglance.walletglance.budget.presentation.model

import androidx.annotation.StringRes

sealed class GroupedCheckedBudgetsItemUiState {

    data class RepeatingPeriodText(
        @StringRes val stringRes: Int
    ) : GroupedCheckedBudgetsItemUiState()

    data class Budget(
        val uiState: BudgetUiState,
        val checked: Boolean
    ) : GroupedCheckedBudgetsItemUiState()

}
