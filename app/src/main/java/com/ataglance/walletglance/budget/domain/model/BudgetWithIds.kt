package com.ataglance.walletglance.budget.domain.model

import com.ataglance.walletglance.core.domain.date.RepeatingPeriod

data class BudgetWithIds(
    val budget: Budget,
    val accountIds: List<Int>
) {

    val budgetId: Int
        get() = budget.id

    val amountLimit: Double
        get() = budget.amountLimit

    val categoryId: Int
        get() = budget.categoryId

    val name: String
        get() = budget.name

    val repeatingPeriod: RepeatingPeriod
        get() = budget.repeatingPeriod

}
