package com.ataglance.walletglance.budget.presentation.component.container

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.BudgetWithIds
import com.ataglance.walletglance.budget.domain.model.GroupedBudgets
import com.ataglance.walletglance.budget.mapper.toUiState
import com.ataglance.walletglance.budget.presentation.component.GroupedCheckedBudgetsComponent
import com.ataglance.walletglance.budget.presentation.viewmodel.BudgetsOnWidgetSettingsViewModel
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.presentation.component.bottomSheet.BottomSheetComponent
import com.ataglance.walletglance.core.presentation.preview.PreviewContainer
import com.ataglance.walletglance.personalization.domain.model.WidgetName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetsOnWidgetSettingsBottomSheet(
    viewModel: BudgetsOnWidgetSettingsViewModel
) {
    val sheetState = rememberModalBottomSheetState()

    val openedWidgetSettings by viewModel.openedWidgetSettings.collectAsStateWithLifecycle()

    val groupedBudgetsItems by viewModel.groupedBudgetsItems.collectAsStateWithLifecycle()
    val limitIsReached by viewModel.checkedBudgetsLimitIsReached.collectAsStateWithLifecycle()

    BottomSheetComponent(
        visible = openedWidgetSettings == WidgetName.ChosenBudgets,
        sheetState = sheetState,
        onDismissRequest = {
            viewModel.saveCurrentBudgetsOnWidget()
            viewModel.closeWidgetSettings()
        },
    ) {
        GroupedCheckedBudgetsComponent(
            groupedBudgetsItems = groupedBudgetsItems,
            limitIsReached = limitIsReached,
            onCheckBudget = viewModel::checkBudgetOnWidget,
            onUncheckBudget = viewModel::uncheckBudgetOnWidget
        )
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun BudgetsOnWidgetSettingsBottomSheetPreview(
    groupedCategoriesByType: GroupedCategoriesByType = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories(),
    accounts: List<Account> = listOf(
        Account(id = 1, orderNum = 1, isActive = true),
        Account(id = 2, orderNum = 2, isActive = false)
    ),
) {
    val limitIsReached = true

    val budgets = listOf(
        BudgetWithIds(
            budget = Budget(
                id = 1,
                amountLimit = 1000.0,
                categoryId = 1,
                name = "Food & Drinks",
                repeatingPeriod = RepeatingPeriod.Daily
            ),
            accountIds = listOf(1)
        ),
        BudgetWithIds(
            budget = Budget(
                id = 2,
                amountLimit = 6000.0,
                categoryId = 2,
                name = "Housing",
                repeatingPeriod = RepeatingPeriod.Weekly
            ),
            accountIds = listOf(2)
        ),
        BudgetWithIds(
            budget = Budget(
                id = 3,
                amountLimit = 4000.0,
                categoryId = 1,
                name = "Food & Drinks",
                repeatingPeriod = RepeatingPeriod.Monthly
            ),
            accountIds = listOf(1)
        ),
        BudgetWithIds(
            budget = Budget(
                id = 4,
                amountLimit = 2000.0,
                categoryId = 3,
                name = "Shopping",
                repeatingPeriod = RepeatingPeriod.Monthly
            ),
            accountIds = listOf(2)
        ),
        BudgetWithIds(
            budget = Budget(
                id = 5,
                amountLimit = 2500.0,
                categoryId = 1,
                name = "Food & Drinks",
                repeatingPeriod = RepeatingPeriod.Yearly
            ),
            accountIds = listOf(1)
        ),
    )
    val checkedBudgetIds = listOf(1, 2)

    val groupedBudgetsItems = GroupedBudgets
        .fromBudgets(budgets = budgets)
        .toUiState(categories = groupedCategoriesByType.expense, accounts = accounts)
        .asCheckedItems(checkedBudgetIds = checkedBudgetIds)

    PreviewContainer {
        GroupedCheckedBudgetsComponent(
            groupedBudgetsItems = groupedBudgetsItems,
            limitIsReached = limitIsReached,
            onCheckBudget = {},
            onUncheckBudget = {}
        )
    }
}