package com.glanci.budget.shared.dto

import kotlinx.serialization.Serializable

@Serializable
data class BudgetWithAssociationsDto(
    val budget: BudgetDto,
    val associations: List<BudgetAccountAssociationDto>
)
