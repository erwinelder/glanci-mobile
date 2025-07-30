package com.ataglance.walletglance.budget.presentation.model

import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod

data class BudgetUiState(
    val id: Int = 0,
    val amountLimit: String = "0.0",
    val category: Category = Category(),
    val priorityNum: Double = 0.0,
    val name: String = "",
    val repeatingPeriod: RepeatingPeriod = RepeatingPeriod.Monthly,
    val currency: String = "",
    val accountIds: List<Int> = emptyList()
) {

    val isNew: Boolean
        get() = id == 0

}
