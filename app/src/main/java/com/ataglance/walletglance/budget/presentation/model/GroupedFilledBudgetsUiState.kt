package com.ataglance.walletglance.budget.presentation.model

import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.domain.date.asStringRes
import com.ataglance.walletglance.core.utils.asList

data class GroupedFilledBudgetsUiState(
    val daily: List<FilledBudgetUiState> = emptyList(),
    val weekly: List<FilledBudgetUiState> = emptyList(),
    val monthly: List<FilledBudgetUiState> = emptyList(),
    val yearly: List<FilledBudgetUiState> = emptyList()
) {

    fun getBudgetsByType(repeatingPeriod: RepeatingPeriod): List<FilledBudgetUiState> {
        return when (repeatingPeriod) {
            RepeatingPeriod.Daily -> daily
            RepeatingPeriod.Weekly -> weekly
            RepeatingPeriod.Monthly -> monthly
            RepeatingPeriod.Yearly -> yearly
        }
    }


    fun asItems(): List<GroupedFilledBudgetsItemUiState> {
        return asItems(repeatingPeriod = RepeatingPeriod.Daily) +
                asItems(repeatingPeriod = RepeatingPeriod.Weekly) +
                asItems(repeatingPeriod = RepeatingPeriod.Monthly) +
                asItems(repeatingPeriod = RepeatingPeriod.Yearly)
    }

    private fun asItems(repeatingPeriod: RepeatingPeriod): List<GroupedFilledBudgetsItemUiState> {
        return getBudgetsByType(repeatingPeriod = repeatingPeriod)
            .takeIf { it.isNotEmpty() }
            ?.let { budgets ->
                val text = GroupedFilledBudgetsItemUiState.RepeatingPeriodText(
                    stringRes = repeatingPeriod.asStringRes()
                )
                val budgets = budgets.map { GroupedFilledBudgetsItemUiState.Budget(uiState = it) }
                text.asList() + budgets
            }
            .orEmpty()
    }

}