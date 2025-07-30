package com.ataglance.walletglance.budget.presentation.model

import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.domain.date.TimestampRange

data class FilledBudgetUiState(
    val id: Int = 0,
    val name: String = "",
    val repeatingPeriod: RepeatingPeriod = RepeatingPeriod.Monthly,
    val amountLimit: String = "0.0",
    val usedAmount: String = "0.0",
    val usedPercentage: Float = 0f,
    val category: Category = Category(),
    val priorityNum: Double = 0.0,
    val currency: String = "",
    val accountIds: List<Int> = emptyList(),
    val dateRange: TimestampRange = TimestampRange(0, 0),
    val currentTimeWithinRangeGraphPercentage: Float = 0f
)
