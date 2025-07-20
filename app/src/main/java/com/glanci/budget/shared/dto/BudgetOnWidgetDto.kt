package com.glanci.budget.shared.dto

import kotlinx.serialization.Serializable

@Serializable
data class BudgetOnWidgetDto(
    val budgetId: Int,
    val timestamp: Long,
    val deleted: Boolean
)