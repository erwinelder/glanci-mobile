package com.ataglance.walletglance.budget.presentation.model

import androidx.annotation.StringRes

sealed class GroupedFilledBudgetsItemUiState {

    data class RepeatingPeriodText(@StringRes val stringRes: Int) : GroupedFilledBudgetsItemUiState()

    data class Budget(val uiState: FilledBudgetUiState) : GroupedFilledBudgetsItemUiState()

}
