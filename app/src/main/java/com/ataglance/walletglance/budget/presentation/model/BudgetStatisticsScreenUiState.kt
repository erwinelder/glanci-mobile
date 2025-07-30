package com.ataglance.walletglance.budget.presentation.model

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.domain.statistics.ColumnChartUiState

data class BudgetStatisticsScreenUiState(
    val id: Int = 0,
    val amountLimit: Double = 0.0,
    val category: Category? = null,
    val budgetName: String = "",
    val repeatingPeriod: RepeatingPeriod = RepeatingPeriod.Monthly,
    val currency: String = "",
    val accounts: List<Account> = emptyList(),
    val columnChartUiState: ColumnChartUiState = ColumnChartUiState()
)
