package com.glanci.budget.shared.dto

import kotlinx.serialization.Serializable

@Serializable
data class BudgetDto(
    val id: Int,
    val amountLimit: Double,
    val categoryId: Int,
    val name: String,
    val repeatingPeriod: String,
    val timestamp: Long,
    val deleted: Boolean
)