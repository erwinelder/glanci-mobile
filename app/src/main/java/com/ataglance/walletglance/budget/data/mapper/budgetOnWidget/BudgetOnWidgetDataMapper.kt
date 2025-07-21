package com.ataglance.walletglance.budget.data.mapper.budgetOnWidget

import com.ataglance.walletglance.budget.data.local.model.BudgetOnWidgetEntity
import com.ataglance.walletglance.budget.data.model.BudgetOnWidgetDataModel
import com.glanci.budget.shared.dto.BudgetOnWidgetDto


fun BudgetOnWidgetDataModel.toEntity(timestamp: Long, deleted: Boolean): BudgetOnWidgetEntity {
    return BudgetOnWidgetEntity(
        budgetId = budgetId,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun BudgetOnWidgetEntity.toDataModel(): BudgetOnWidgetDataModel {
    return BudgetOnWidgetDataModel(
        budgetId = budgetId
    )
}

fun BudgetOnWidgetEntity.toDto(): BudgetOnWidgetDto {
    return BudgetOnWidgetDto(
        budgetId = budgetId,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun BudgetOnWidgetDto.toEntity(): BudgetOnWidgetEntity {
    return BudgetOnWidgetEntity(
        budgetId = budgetId,
        timestamp = timestamp,
        deleted = deleted
    )
}
