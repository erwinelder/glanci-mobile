package com.ataglance.walletglance.budget.presentation.model

import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.domain.date.asStringRes
import com.ataglance.walletglance.core.utils.asList

data class GroupedBudgetsUiState(
    val daily: List<BudgetUiState> = emptyList(),
    val weekly: List<BudgetUiState> = emptyList(),
    val monthly: List<BudgetUiState> = emptyList(),
    val yearly: List<BudgetUiState> = emptyList()
) {

    companion object {

        fun fromBudgets(budgets: List<BudgetUiState>): GroupedBudgetsUiState {
            val groupedBudgets = budgets
                .groupBy { it.repeatingPeriod }
                .mapValues { entry ->
                    entry.value.sortedBy { it.priorityNum }
                }

            return GroupedBudgetsUiState(
                daily = groupedBudgets[RepeatingPeriod.Daily].orEmpty(),
                weekly = groupedBudgets[RepeatingPeriod.Weekly].orEmpty(),
                monthly = groupedBudgets[RepeatingPeriod.Monthly].orEmpty(),
                yearly = groupedBudgets[RepeatingPeriod.Yearly].orEmpty()
            )
        }

    }


    fun getBudgetsByType(repeatingPeriod: RepeatingPeriod): List<BudgetUiState> {
        return when (repeatingPeriod) {
            RepeatingPeriod.Daily -> daily
            RepeatingPeriod.Weekly -> weekly
            RepeatingPeriod.Monthly -> monthly
            RepeatingPeriod.Yearly -> yearly
        }
    }

    fun replaceBudgetsByType(
        budgets: List<BudgetUiState>,
        type: RepeatingPeriod
    ): GroupedBudgetsUiState {
        return when (type) {
            RepeatingPeriod.Daily -> this.copy(daily = budgets)
            RepeatingPeriod.Weekly -> this.copy(weekly = budgets)
            RepeatingPeriod.Monthly -> this.copy(monthly = budgets)
            RepeatingPeriod.Yearly -> this.copy(yearly = budgets)
        }
    }

    fun addBudget(budget: BudgetUiState): GroupedBudgetsUiState {
        return getBudgetsByType(repeatingPeriod = budget.repeatingPeriod)
            .addBudgetAndSort(budget = budget)
            .let { replaceBudgetsByType(budgets = it, type = budget.repeatingPeriod) }
    }

    private fun List<BudgetUiState>.addBudgetAndSort(budget: BudgetUiState): List<BudgetUiState> {
        return (this + budget).sortedBy { it.priorityNum }
    }

    fun replaceBudget(budget: BudgetUiState): GroupedBudgetsUiState {
        val budgets = concatenate().map { if (it.id == budget.id) budget else it }
        return fromBudgets(budgets = budgets)
    }

    fun deleteBudget(id: Int, repeatingPeriod: RepeatingPeriod): GroupedBudgetsUiState {
        val newBudgets = getBudgetsByType(repeatingPeriod = repeatingPeriod).filter { it.id != id }
        return replaceBudgetsByType(budgets = newBudgets, type = repeatingPeriod)
    }


    fun concatenate(): List<BudgetUiState> {
        return daily + weekly + monthly + yearly
    }


    fun asItems(): List<GroupedBudgetsItemUiState> {
        return asItems(repeatingPeriod = RepeatingPeriod.Daily) +
                asItems(repeatingPeriod = RepeatingPeriod.Weekly) +
                asItems(repeatingPeriod = RepeatingPeriod.Monthly) +
                asItems(repeatingPeriod = RepeatingPeriod.Yearly)
    }

    fun asCheckedItems(checkedBudgetIds: List<Int>): List<GroupedCheckedBudgetsItemUiState> {
        return asCheckedItems(repeatingPeriod = RepeatingPeriod.Daily, checkedBudgetIds = checkedBudgetIds) +
                asCheckedItems(repeatingPeriod = RepeatingPeriod.Weekly, checkedBudgetIds = checkedBudgetIds) +
                asCheckedItems(repeatingPeriod = RepeatingPeriod.Monthly, checkedBudgetIds = checkedBudgetIds) +
                asCheckedItems(repeatingPeriod = RepeatingPeriod.Yearly, checkedBudgetIds = checkedBudgetIds)
    }

    private fun asItems(repeatingPeriod: RepeatingPeriod): List<GroupedBudgetsItemUiState> {
        return getBudgetsByType(repeatingPeriod = repeatingPeriod)
            .takeIf { it.isNotEmpty() }
            ?.let { budgets ->
                val text = GroupedBudgetsItemUiState.RepeatingPeriodText(
                    stringRes = repeatingPeriod.asStringRes()
                )
                val budgets = budgets.map { GroupedBudgetsItemUiState.Budget(uiState = it) }
                text.asList() + budgets
            }
            .orEmpty()
    }

    private fun asCheckedItems(
        repeatingPeriod: RepeatingPeriod,
        checkedBudgetIds: List<Int>
    ): List<GroupedCheckedBudgetsItemUiState> {
        return getBudgetsByType(repeatingPeriod = repeatingPeriod)
            .takeIf { it.isNotEmpty() }
            ?.let { budgets ->
                val text = GroupedCheckedBudgetsItemUiState.RepeatingPeriodText(
                    stringRes = repeatingPeriod.asStringRes()
                )
                val budgets = budgets.map {
                    GroupedCheckedBudgetsItemUiState.Budget(
                        uiState = it, checked = it.id in checkedBudgetIds
                    )
                }
                text.asList() + budgets
            }
            .orEmpty()
    }

}