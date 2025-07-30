package com.ataglance.walletglance.budget.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.BudgetWithIds
import com.ataglance.walletglance.budget.domain.model.GroupedBudgets
import com.ataglance.walletglance.budget.domain.model.GroupedFilledBudgets
import com.ataglance.walletglance.budget.mapper.budget.toUiState
import com.ataglance.walletglance.budget.presentation.component.GroupedFilledBudgetsComponent
import com.ataglance.walletglance.budget.presentation.model.FilledBudgetUiState
import com.ataglance.walletglance.budget.presentation.model.GroupedFilledBudgetsItemUiState
import com.ataglance.walletglance.budget.presentation.viewmodel.BudgetsViewModel
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.domain.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.component.container.MessageContainer
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import com.ataglance.walletglance.core.presentation.model.ResourceManagerImpl
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.theme.CurrWindowType
import com.ataglance.walletglance.core.presentation.utils.plus
import com.ataglance.walletglance.core.utils.getCurrentTimestamp
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.transaction.domain.model.Record
import com.ataglance.walletglance.transaction.domain.model.RecordItem
import com.ataglance.walletglance.transaction.domain.model.RecordWithItems
import com.ataglance.walletglance.transaction.domain.model.Transaction
import com.ataglance.walletglance.transaction.domain.model.Transfer
import com.ataglance.walletglance.transaction.domain.model.TransferItem
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BudgetsScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavHostController,
    navViewModel: NavigationViewModel
) {
    val resourceManager = koinInject<ResourceManager>()
    val viewModel = koinViewModel<BudgetsViewModel>()

    val groupedBudgetsItems by viewModel.groupedBudgetsItems.collectAsStateWithLifecycle()

    BudgetsScreen(
        screenPadding = screenPadding,
        groupedBudgetsItems = groupedBudgetsItems,
        resourceManager = resourceManager,
        onBudgetClick = { budget ->
            navViewModel.navigateToScreenMovingTowardsLeft(
                navController = navController, screen = MainScreens.BudgetStatistics(budget.id)
            )
        }
    )
}

@Composable
fun BudgetsScreen(
    screenPadding: PaddingValues = PaddingValues(),
    groupedBudgetsItems: List<GroupedFilledBudgetsItemUiState>,
    resourceManager: ResourceManager,
    onBudgetClick: (FilledBudgetUiState) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        if (groupedBudgetsItems.isNotEmpty()) {
            GroupedFilledBudgetsComponent(
                groupedBudgetsItems = groupedBudgetsItems,
                resourceManager = resourceManager,
                contentPadding = screenPadding + PaddingValues(vertical = 24.dp),
                textDividerFilledWidth = FilledWidthByScreenType(.76f).get(CurrWindowType),
                onBudgetClick = onBudgetClick
            )
        } else {
            MessageContainer(message = stringResource(R.string.you_have_no_budgets_yet))
        }
    }
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun BudgetsScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    groupedCategoriesByType: GroupedCategoriesByType = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories(),
    accounts: List<Account> = listOf(
        Account(id = 1, orderNum = 1, currency = "USD", isActive = true),
        Account(id = 2, orderNum = 2, currency = "CZK", isActive = false)
    ),
    transactions: List<Transaction> = listOf(
        RecordWithItems(
            record = Record(
                id = 1,
                date = getCurrentTimestamp(),
                type = CategoryType.Expense,
                accountId = accounts[0].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 1,
                    recordId = 1,
                    totalAmount = 68.43,
                    quantity = null,
                    categoryId = 1,
                    subcategoryId = 13,
                    note = "bread, milk"
                ),
                RecordItem(
                    id = 2,
                    recordId = 1,
                    totalAmount = 178.9,
                    quantity = null,
                    categoryId = 3,
                    subcategoryId = 24,
                    note = "shampoo"
                )
            )
        ),
        Transfer(
            id = 1,
            date = getCurrentTimestamp(),
            sender = TransferItem(
                accountId = accounts[0].id,
                amount = 3000.0,
                rate = 1.0
            ),
            receiver = TransferItem(
                accountId = accounts[1].id,
                amount = 3000.0,
                rate = 1.0
            ),
            includeInBudgets = true
        ),
        RecordWithItems(
            record = Record(
                id = 4,
                date = getCurrentTimestamp(),
                type = CategoryType.Expense,
                accountId = accounts[0].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 4,
                    recordId = 4,
                    totalAmount = 120.9,
                    quantity = null,
                    categoryId = 6,
                    subcategoryId = 40,
                    note = "Music platform"
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 5,
                date = getCurrentTimestamp(),
                type = CategoryType.Expense,
                accountId = accounts[0].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 5,
                    recordId = 5,
                    totalAmount = 799.9,
                    quantity = null,
                    categoryId = 3,
                    subcategoryId = 21,
                    note = null
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 6,
                date = getCurrentTimestamp(),
                type = CategoryType.Expense,
                accountId = accounts[0].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 6,
                    recordId = 6,
                    totalAmount = 3599.9,
                    quantity = null,
                    categoryId = 1,
                    subcategoryId = 13,
                    note = null
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 7,
                date = getCurrentTimestamp(),
                type = CategoryType.Expense,
                accountId = accounts[0].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 7,
                    recordId = 7,
                    totalAmount = 8500.0,
                    quantity = null,
                    categoryId = 2,
                    subcategoryId = 15,
                    note = null
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 8,
                date = getCurrentTimestamp(),
                type = CategoryType.Income,
                accountId = accounts[0].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 8,
                    recordId = 8,
                    totalAmount = 42600.0,
                    quantity = null,
                    categoryId = 72,
                    subcategoryId = null,
                    note = null
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 9,
                date = getCurrentTimestamp(),
                type = CategoryType.Expense,
                accountId = accounts[0].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 9,
                    recordId = 9,
                    totalAmount = 799.9,
                    quantity = null,
                    categoryId = 6,
                    subcategoryId = 38,
                    note = null
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 10,
                date = getCurrentTimestamp(),
                type = CategoryType.Expense,
                accountId = accounts[1].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 10,
                    recordId = 10,
                    totalAmount = 450.41,
                    quantity = null,
                    categoryId = 9,
                    subcategoryId = 50,
                    note = null
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 10,
                date = getCurrentTimestamp(),
                type = CategoryType.Expense,
                accountId = accounts[0].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 10,
                    recordId = 10,
                    totalAmount = 690.56,
                    quantity = null,
                    categoryId = 10,
                    subcategoryId = 58,
                    note = null
                )
            )
        ),
    ),
) {
    val budgets = listOf(
        BudgetWithIds(
            budget = Budget(
                id = 1,
                amountLimit = 4000.0,
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
            accountIds = listOf(1)
        ),
        BudgetWithIds(
            budget = Budget(
                id = 3,
                amountLimit = 10000.0,
                categoryId = 1,
                name = "Food & Drinks",
                repeatingPeriod = RepeatingPeriod.Monthly
            ),
            accountIds = listOf(1)
        ),
        BudgetWithIds(
            budget = Budget(
                id = 4,
                amountLimit = 1500.0,
                categoryId = 3,
                name = "Shopping",
                repeatingPeriod = RepeatingPeriod.Monthly
            ),
            accountIds = listOf(1)
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

    val groupedBudgets = GroupedBudgets.fromBudgets(budgets = budgets)
    val groupedBudgetsItems = GroupedFilledBudgets
        .fromBudgetsAndTransactions(
            groupedBudgets = groupedBudgets,
            transactions = transactions
        )
        .toUiState(categories = groupedCategoriesByType.expense, accounts = accounts)
        .asItems()

    PreviewWithMainScaffoldContainer(appTheme = appTheme) { scaffoldPadding ->
        BudgetsScreen(
            screenPadding = scaffoldPadding,
            groupedBudgetsItems = groupedBudgetsItems,
            resourceManager = ResourceManagerImpl(context = LocalContext.current),
            onBudgetClick = {}
        )
    }
}