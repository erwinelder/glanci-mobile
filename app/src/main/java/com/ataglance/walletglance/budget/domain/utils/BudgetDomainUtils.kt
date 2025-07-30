package com.ataglance.walletglance.budget.domain.utils

import com.ataglance.walletglance.budget.domain.model.BudgetWithIds
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.core.utils.toTimestampRange
import com.ataglance.walletglance.transaction.domain.model.Transaction


fun List<BudgetWithIds>.getMaxRepeatingPeriod(): RepeatingPeriod? {
    val budget = find { it.repeatingPeriod == RepeatingPeriod.Yearly }
        ?: find { it.repeatingPeriod == RepeatingPeriod.Monthly }
        ?: find { it.repeatingPeriod == RepeatingPeriod.Weekly }
        ?: find { it.repeatingPeriod == RepeatingPeriod.Daily }

    return budget?.repeatingPeriod
}

fun List<BudgetWithIds>.getMaxDateRange(): TimestampRange? {
    return getMaxRepeatingPeriod()?.toTimestampRange()
}


fun List<Transaction>.filterByRepeatingPeriod(period: RepeatingPeriod): List<Transaction> {
    val dateRange = period.toTimestampRange()
    return filter { dateRange.containsDate(date = it.date) }
}


fun List<Transaction>.getTotalAmountByBudget(budget: BudgetWithIds): Double {
    if (budget.accountIds.isEmpty()) return 0.0

    return sumOf { transaction ->
        transaction
            .takeIf { it.includeInBudgets }
            ?.getTotalExpensesByAccountsAndCategory(
                accountIds = budget.accountIds, categoryId = budget.categoryId
            )
            ?: 0.0
    }
}
