package com.ataglance.walletglance.budget.presentation.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.BudgetWithIds
import com.ataglance.walletglance.budget.mapper.toUiState
import com.ataglance.walletglance.budget.presentation.component.GroupedBudgetsComponent
import com.ataglance.walletglance.budget.presentation.model.BudgetUiState
import com.ataglance.walletglance.budget.presentation.model.GroupedBudgetsItemUiState
import com.ataglance.walletglance.budget.presentation.model.GroupedBudgetsUiState
import com.ataglance.walletglance.budget.presentation.navigation.BudgetsSettingsScreens
import com.ataglance.walletglance.budget.presentation.viewmodel.EditBudgetViewModel
import com.ataglance.walletglance.budget.presentation.viewmodel.EditBudgetsViewModel
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.presentation.component.button.SmallSecondaryButton
import com.ataglance.walletglance.core.presentation.component.container.MessageContainer
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurface
import com.ataglance.walletglance.core.presentation.component.screenContainer.ScreenContainerWithTopBackNavButtonAndPrimaryButton
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.viewmodel.sharedKoinNavViewModel
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.presentation.model.SettingsCategory
import kotlinx.coroutines.launch

@Composable
fun EditBudgetsScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    backStack: NavBackStackEntry,
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    isAppSetUp: Boolean
) {
    val budgetsViewModel = backStack.sharedKoinNavViewModel<EditBudgetsViewModel>(navController)
    val budgetViewModel = backStack.sharedKoinNavViewModel<EditBudgetViewModel>(navController)

    val groupedBudgetsItems by budgetsViewModel.groupedBudgetsItems.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    EditBudgetsScreen(
        screenPadding = screenPadding,
        onNavigateBack = navController::popBackStack,
        isAppSetUp = isAppSetUp,
        groupedBudgetsItems = groupedBudgetsItems,
        onNavigateToEditBudgetScreen = { budget: BudgetUiState? ->
            budgetViewModel.applyBudget(budget)
            navViewModel.navigate(navController, BudgetsSettingsScreens.EditBudget)
        },
        onSaveBudgetsButton = {
            coroutineScope.launch {
                budgetsViewModel.saveBudgets()
                if (isAppSetUp) {
                    navController.popBackStack()
                } else {
                    budgetsViewModel.preFinishSetup()
                }
            }
        }
    )
}

@Composable
fun EditBudgetsScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    isAppSetUp: Boolean,
    groupedBudgetsItems: List<GroupedBudgetsItemUiState>,
    onNavigateToEditBudgetScreen: (BudgetUiState?) -> Unit,
    onSaveBudgetsButton: () -> Unit,
) {
    val settingsCategory = SettingsCategory.Budgets(appTheme = CurrAppTheme)

    ScreenContainerWithTopBackNavButtonAndPrimaryButton(
        screenPadding = screenPadding,
        backNavButtonText = stringResource(settingsCategory.stringRes),
        backNavButtonImageRes = settingsCategory.iconRes,
        onBackNavButtonClick = onNavigateBack,
        primaryButtonText = stringResource(
            if (isAppSetUp) R.string.save
            else if (groupedBudgetsItems.isEmpty()) R.string.finish
            else R.string.save_and_finish
        ),
        onPrimaryButtonClick = onSaveBudgetsButton
    ) {

        GlassSurface(
            modifier = Modifier.weight(1f)
        ) {
            GlassSurfaceContent(
                groupedBudgetsItems = groupedBudgetsItems,
                onBudgetClick = onNavigateToEditBudgetScreen,
            )
        }

        SmallSecondaryButton(
            text = stringResource(R.string.add_budget),
            iconRes = R.drawable.add_icon,
        ) {
            onNavigateToEditBudgetScreen(null)
        }

    }
}

@Composable
private fun GlassSurfaceContent(
    groupedBudgetsItems: List<GroupedBudgetsItemUiState>,
    onBudgetClick: (BudgetUiState) -> Unit,
) {
    if (groupedBudgetsItems.isNotEmpty()) {
        GroupedBudgetsComponent(
            groupedBudgetsItems = groupedBudgetsItems,
            onBudgetClick = onBudgetClick
        )
    } else {
        MessageContainer(message = stringResource(R.string.you_have_no_budgets_yet))
    }
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun EditBudgetsScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = true,
    groupedCategoriesByType: GroupedCategoriesByType = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories(),

    accounts: List<Account> = listOf(
        Account(id = 1, orderNum = 1, isActive = true),
        Account(id = 2, orderNum = 2, isActive = false)
    ),
) {

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
    ).mapNotNull {
        it.toUiState(categories = groupedCategoriesByType.expense, accounts = accounts)
    }
    
    val groupedBudgetsItems = GroupedBudgetsUiState
        .fromBudgets(budgets = budgets)
        .asItems()

    PreviewWithMainScaffoldContainer(appTheme = appTheme) { scaffoldPadding ->
        EditBudgetsScreen(
            screenPadding = scaffoldPadding,
            onNavigateBack = {},
            isAppSetUp = isAppSetUp,
            groupedBudgetsItems = groupedBudgetsItems,
            onNavigateToEditBudgetScreen = {},
            onSaveBudgetsButton = {}
        )
    }
}