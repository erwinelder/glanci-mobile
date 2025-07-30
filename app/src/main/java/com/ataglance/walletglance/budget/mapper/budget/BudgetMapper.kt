package com.ataglance.walletglance.budget.mapper.budget

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.budget.data.model.BudgetAccountAssociationDataModel
import com.ataglance.walletglance.budget.data.model.BudgetDataModel
import com.ataglance.walletglance.budget.data.model.BudgetWithAssociationsDataModel
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.BudgetWithIds
import com.ataglance.walletglance.budget.domain.model.FilledBudget
import com.ataglance.walletglance.budget.domain.model.GroupedBudgets
import com.ataglance.walletglance.budget.domain.model.GroupedFilledBudgets
import com.ataglance.walletglance.budget.presentation.model.BudgetUiState
import com.ataglance.walletglance.budget.presentation.model.FilledBudgetUiState
import com.ataglance.walletglance.budget.presentation.model.GroupedBudgetsUiState
import com.ataglance.walletglance.budget.presentation.model.GroupedFilledBudgetsUiState
import com.ataglance.walletglance.category.domain.model.GroupedCategories
import com.ataglance.walletglance.category.domain.utils.getCategoryWithSubcategoryById
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.utils.enumValueOrNull
import com.ataglance.walletglance.core.utils.formatWithSpaces
import com.ataglance.walletglance.core.utils.roundToTwoDecimalsAsFloat


fun BudgetDataModel.toDomainModel(): Budget? {
    val repeatingPeriod = enumValueOrNull<RepeatingPeriod>(repeatingPeriod) ?: return null

    return Budget(
        id = id,
        amountLimit = amountLimit,
        categoryId = categoryId,
        name = name,
        repeatingPeriod = repeatingPeriod
    )
}

fun BudgetWithAssociationsDataModel.toBudgetWithIds(): BudgetWithIds? {
    return BudgetWithIds(
        budget = budget.toDomainModel() ?: return null,
        accountIds = associations.map { it.accountId }
    )
}


fun Budget.toDataModel(): BudgetDataModel {
    return BudgetDataModel(
        id = id,
        amountLimit = amountLimit,
        categoryId = categoryId,
        name = name,
        repeatingPeriod = repeatingPeriod.name
    )
}

fun Budget.toDataModelWithAssociations(
    associations: List<BudgetAccountAssociationDataModel> = emptyList()
): BudgetWithAssociationsDataModel {
    return BudgetWithAssociationsDataModel(
        budget = toDataModel(),
        associations = associations
    )
}

fun BudgetWithIds.toDataModelWithAssociations(): BudgetWithAssociationsDataModel {
    return BudgetWithAssociationsDataModel(
        budget = budget.toDataModel(),
        associations = accountIds.map { accountId ->
            BudgetAccountAssociationDataModel(budgetId = budgetId, accountId = accountId)
        }
    )
}


fun BudgetWithIds.toUiState(
    categories: List<GroupedCategories>,
    accounts: List<Account>
): BudgetUiState? {
    val categoryWithSubcategory = categories.getCategoryWithSubcategoryById(id = budget.categoryId)
    val priorityNum = categoryWithSubcategory?.groupParentAndSubcategoryOrderNums() ?: 0.0
    val category = categoryWithSubcategory?.getSubcategoryOrCategory() ?: return null

    val currency = accounts.find { it.id in accountIds }?.currency ?: ""

    return BudgetUiState(
        id = budgetId,
        amountLimit = amountLimit.formatWithSpaces(),
        category = category,
        priorityNum = priorityNum,
        name = name,
        repeatingPeriod = repeatingPeriod,
        currency = currency,
        accountIds = accountIds
    )
}

fun GroupedBudgets.toUiState(
    categories: List<GroupedCategories>,
    accounts: List<Account>
): GroupedBudgetsUiState {
    return GroupedBudgetsUiState(
        daily = daily.mapNotNull { it.toUiState(categories = categories, accounts = accounts) },
        weekly = weekly.mapNotNull { it.toUiState(categories = categories, accounts = accounts) },
        monthly = monthly.mapNotNull { it.toUiState(categories = categories, accounts = accounts) },
        yearly = yearly.mapNotNull { it.toUiState(categories = categories, accounts = accounts) }
    )
}


fun FilledBudget.toUiState(
    categories: List<GroupedCategories>,
    accounts: List<Account>
): FilledBudgetUiState? {
    val categoryWithSubcategory = categories.getCategoryWithSubcategoryById(id = budget.categoryId)
    val priorityNum = categoryWithSubcategory?.groupParentAndSubcategoryOrderNums() ?: 0.0
    val category = categoryWithSubcategory?.getSubcategoryOrCategory() ?: return null

    val currency = accounts.find { it.id in accountIds }?.currency ?: ""

    return FilledBudgetUiState(
        id = budget.id,
        name = budget.name,
        repeatingPeriod = budget.repeatingPeriod,
        amountLimit = budget.amountLimit.formatWithSpaces(),
        usedAmount = usedAmount.formatWithSpaces(),
        usedPercentage = (usedAmount / budget.amountLimit).roundToTwoDecimalsAsFloat(),
        category = category,
        priorityNum = priorityNum,
        currency = currency,
        accountIds = accountIds,
        dateRange = dateRange,
        currentTimeWithinRangeGraphPercentage = dateRange.getCurrentDateAsGraphPercentage()
    )
}

fun GroupedFilledBudgets.toUiState(
    categories: List<GroupedCategories>,
    accounts: List<Account>
): GroupedFilledBudgetsUiState {
    return GroupedFilledBudgetsUiState(
        daily = daily.mapNotNull { it.toUiState(categories = categories, accounts = accounts) },
        weekly = weekly.mapNotNull { it.toUiState(categories = categories, accounts = accounts) },
        monthly = monthly.mapNotNull { it.toUiState(categories = categories, accounts = accounts) },
        yearly = yearly.mapNotNull { it.toUiState(categories = categories, accounts = accounts) }
    )
}


fun BudgetUiState.toDomainModel(): Budget? {
    return Budget(
        id = id,
        amountLimit = amountLimit.toDoubleOrNull() ?: return null,
        categoryId = category.id,
        name = name,
        repeatingPeriod = repeatingPeriod
    )
}

fun BudgetUiState.toBudgetWithIds(): BudgetWithIds? {
    return BudgetWithIds(
        budget = toDomainModel() ?: return null,
        accountIds = accountIds
    )
}
