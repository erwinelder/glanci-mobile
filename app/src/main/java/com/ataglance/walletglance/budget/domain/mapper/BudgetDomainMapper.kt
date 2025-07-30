package com.ataglance.walletglance.budget.domain.mapper

import com.ataglance.walletglance.budget.domain.model.BudgetWithIds
import com.ataglance.walletglance.budget.domain.model.FilledBudget
import com.ataglance.walletglance.budget.domain.utils.getTotalAmountByBudget
import com.ataglance.walletglance.core.utils.toTimestampRange
import com.ataglance.walletglance.transaction.domain.model.Transaction


fun BudgetWithIds.toFilledBudgetFiltered(transactions: List<Transaction>): FilledBudget {
    val dateRange = repeatingPeriod.toTimestampRange()

    return toFilledBudget(
        transactions = transactions.filter { dateRange.containsDate(date = it.date) }
    )
}

fun BudgetWithIds.toFilledBudget(transactions: List<Transaction>): FilledBudget {
    return FilledBudget(
        budget = budget,
        accountIds = accountIds,
        dateRange = repeatingPeriod.toTimestampRange(),
        usedAmount = transactions.getTotalAmountByBudget(budget = this)
    )
}
