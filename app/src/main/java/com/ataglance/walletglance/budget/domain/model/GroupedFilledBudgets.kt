package com.ataglance.walletglance.budget.domain.model

import com.ataglance.walletglance.budget.domain.utils.filterByRepeatingPeriod
import com.ataglance.walletglance.budget.domain.mapper.toFilledBudget
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.transaction.domain.model.Transaction

data class GroupedFilledBudgets(
    val daily: List<FilledBudget> = emptyList(),
    val weekly: List<FilledBudget> = emptyList(),
    val monthly: List<FilledBudget> = emptyList(),
    val yearly: List<FilledBudget> = emptyList()
) {

    companion object {

        fun fromBudgetsAndTransactions(
            groupedBudgets: GroupedBudgets,
            transactions: List<Transaction>
        ): GroupedFilledBudgets {

            var transactions = transactions.filterByRepeatingPeriod(RepeatingPeriod.Yearly)
            val filledYearlyBudgets = groupedBudgets.getByType(RepeatingPeriod.Yearly)
                .map { it.toFilledBudget(transactions = transactions) }

            transactions = transactions.filterByRepeatingPeriod(RepeatingPeriod.Monthly)
            val filledMonthlyBudgets = groupedBudgets.getByType(RepeatingPeriod.Monthly)
                .map { it.toFilledBudget(transactions = transactions) }

            transactions = transactions.filterByRepeatingPeriod(RepeatingPeriod.Weekly)
            val filledWeeklyBudgets = groupedBudgets.getByType(RepeatingPeriod.Weekly)
                .map { it.toFilledBudget(transactions = transactions) }

            transactions = transactions.filterByRepeatingPeriod(RepeatingPeriod.Daily)
            val filledDailyBudgets = groupedBudgets.getByType(RepeatingPeriod.Daily)
                .map { it.toFilledBudget(transactions = transactions) }

            return GroupedFilledBudgets(
                daily = filledDailyBudgets,
                weekly = filledWeeklyBudgets,
                monthly = filledMonthlyBudgets,
                yearly = filledYearlyBudgets
            )
        }

    }

}
