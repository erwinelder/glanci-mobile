package com.glanci.budget.shared.dto

import kotlinx.serialization.Serializable

@Serializable
data class BudgetAccountAssociationDto(
    val budgetId: Int,
    val accountId: Int
)
