package com.ataglance.walletglance.budget.data.mapper.budget

import com.ataglance.walletglance.budget.data.local.model.BudgetAccountAssociationEntity
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity
import com.ataglance.walletglance.budget.data.local.model.BudgetEntityWithAssociations
import com.ataglance.walletglance.budget.data.model.BudgetAccountAssociationDataModel
import com.ataglance.walletglance.budget.data.model.BudgetDataModel
import com.ataglance.walletglance.budget.data.model.BudgetWithAssociationsDataModel
import com.glanci.budget.shared.dto.BudgetAccountAssociationDto
import com.glanci.budget.shared.dto.BudgetDto
import com.glanci.budget.shared.dto.BudgetWithAssociationsDto


fun BudgetDataModel.withAssociations(
    associations: List<BudgetAccountAssociationDataModel> = emptyList()
): BudgetWithAssociationsDataModel {
    return BudgetWithAssociationsDataModel(
        budget = this,
        associations = associations
    )
}


fun BudgetDataModel.toEntity(timestamp: Long, deleted: Boolean): BudgetEntity {
    return BudgetEntity(
        id = id,
        amountLimit = amountLimit,
        categoryId = categoryId,
        name = name,
        repeatingPeriod = repeatingPeriod,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun BudgetAccountAssociationDataModel.toEntity(): BudgetAccountAssociationEntity {
    return BudgetAccountAssociationEntity(budgetId = budgetId, accountId = accountId)
}

fun BudgetWithAssociationsDataModel.toEntityWithAssociations(
    timestamp: Long,
    deleted: Boolean
): BudgetEntityWithAssociations {
    return BudgetEntityWithAssociations(
        budget = budget.toEntity(timestamp = timestamp, deleted = deleted),
        associations = associations.map { it.toEntity() }
    )
}


fun BudgetEntity.toDataModel(): BudgetDataModel {
    return BudgetDataModel(
        id = id,
        amountLimit = amountLimit,
        categoryId = categoryId,
        name = name,
        repeatingPeriod = repeatingPeriod
    )
}

fun BudgetAccountAssociationEntity.toDataModel(): BudgetAccountAssociationDataModel {
    return BudgetAccountAssociationDataModel(budgetId = budgetId, accountId = accountId)
}

fun BudgetEntityWithAssociations.toDataModelWithAssociations(): BudgetWithAssociationsDataModel {
    return BudgetWithAssociationsDataModel(
        budget = budget.toDataModel(),
        associations = associations.map { it.toDataModel() }
    )
}


fun BudgetEntity.toDto(): BudgetDto {
    return BudgetDto(
        id = id,
        amountLimit = amountLimit,
        categoryId = categoryId,
        name = name,
        repeatingPeriod = repeatingPeriod,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun BudgetAccountAssociationEntity.toDto(): BudgetAccountAssociationDto {
    return BudgetAccountAssociationDto(budgetId = budgetId, accountId = accountId)
}

fun BudgetEntityWithAssociations.toDtoWithAssociations(): BudgetWithAssociationsDto {
    return BudgetWithAssociationsDto(
        budget = budget.toDto(),
        associations = associations.map { it.toDto() }
    )
}


fun BudgetDto.toEntity(): BudgetEntity {
    return BudgetEntity(
        id = id,
        amountLimit = amountLimit,
        categoryId = categoryId,
        name = name,
        repeatingPeriod = repeatingPeriod,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun BudgetAccountAssociationDto.toEntity(): BudgetAccountAssociationEntity {
    return BudgetAccountAssociationEntity(budgetId = budgetId, accountId = accountId)
}

fun BudgetWithAssociationsDto.toEntityWithAssociations(): BudgetEntityWithAssociations {
    return BudgetEntityWithAssociations(
        budget = budget.toEntity(),
        associations = associations.map { it.toEntity() }
    )
}
