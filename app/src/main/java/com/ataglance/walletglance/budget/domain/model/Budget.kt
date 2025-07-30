package com.ataglance.walletglance.budget.domain.model

import com.ataglance.walletglance.core.domain.date.RepeatingPeriod

data class Budget(
    val id: Int,
    val amountLimit: Double,
    val categoryId: Int,
    val name: String,
    val repeatingPeriod: RepeatingPeriod
)
