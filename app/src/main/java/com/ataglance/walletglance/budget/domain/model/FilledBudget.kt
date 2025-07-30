package com.ataglance.walletglance.budget.domain.model

import com.ataglance.walletglance.core.domain.date.TimestampRange

data class FilledBudget(
    val budget: Budget,
    val accountIds: List<Int>,
    val dateRange: TimestampRange,
    val usedAmount: Double
)
