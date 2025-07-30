package com.ataglance.walletglance.di

import com.ataglance.walletglance.budget.data.local.source.BudgetLocalDataSource
import com.ataglance.walletglance.budget.data.local.source.BudgetOnWidgetLocalDataSource
import com.ataglance.walletglance.budget.data.local.source.getBudgetLocalDataSource
import com.ataglance.walletglance.budget.data.local.source.getBudgetOnWidgetLocalDataSource
import com.ataglance.walletglance.budget.data.remote.source.BudgetOnWidgetRemoteDataSource
import com.ataglance.walletglance.budget.data.remote.source.BudgetOnWidgetRemoteDataSourceImpl
import com.ataglance.walletglance.budget.data.remote.source.BudgetRemoteDataSource
import com.ataglance.walletglance.budget.data.remote.source.BudgetRemoteDataSourceImpl
import com.ataglance.walletglance.budget.data.repository.BudgetOnWidgetRepository
import com.ataglance.walletglance.budget.data.repository.BudgetOnWidgetRepositoryImpl
import com.ataglance.walletglance.budget.data.repository.BudgetRepository
import com.ataglance.walletglance.budget.data.repository.BudgetRepositoryImpl
import com.ataglance.walletglance.budget.domain.usecase.GetBudgetIdsOnWidgetUseCase
import com.ataglance.walletglance.budget.domain.usecase.GetBudgetIdsOnWidgetUseCaseImpl
import com.ataglance.walletglance.budget.domain.usecase.GetBudgetUseCase
import com.ataglance.walletglance.budget.domain.usecase.GetBudgetUseCaseImpl
import com.ataglance.walletglance.budget.domain.usecase.GetBudgetsOnWidgetUseCase
import com.ataglance.walletglance.budget.domain.usecase.GetBudgetsOnWidgetUseCaseImpl
import com.ataglance.walletglance.budget.domain.usecase.GetBudgetsUseCase
import com.ataglance.walletglance.budget.domain.usecase.GetBudgetsUseCaseImpl
import com.ataglance.walletglance.budget.domain.usecase.GetGroupedBudgetsUseCase
import com.ataglance.walletglance.budget.domain.usecase.GetGroupedBudgetsUseCaseImpl
import com.ataglance.walletglance.budget.domain.usecase.GetGroupedFilledBudgetsUseCase
import com.ataglance.walletglance.budget.domain.usecase.GetGroupedFilledBudgetsUseCaseImpl
import com.ataglance.walletglance.budget.domain.usecase.SaveBudgetsAndDeleteRestUseCase
import com.ataglance.walletglance.budget.domain.usecase.SaveBudgetsAndDeleteRestUseCaseImpl
import com.ataglance.walletglance.budget.domain.usecase.SaveBudgetsOnWidgetAndDeleteRestUseCase
import com.ataglance.walletglance.budget.domain.usecase.SaveBudgetsOnWidgetAndDeleteRestUseCaseImpl
import com.ataglance.walletglance.budget.presentation.viewmodel.BudgetStatisticsViewModel
import com.ataglance.walletglance.budget.presentation.viewmodel.BudgetsOnWidgetSettingsViewModel
import com.ataglance.walletglance.budget.presentation.viewmodel.BudgetsOnWidgetViewModel
import com.ataglance.walletglance.budget.presentation.viewmodel.BudgetsViewModel
import com.ataglance.walletglance.budget.presentation.viewmodel.EditBudgetViewModel
import com.ataglance.walletglance.budget.presentation.viewmodel.EditBudgetsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val budgetModule = module {

    /* ---------- Data Sources ---------- */

    single<BudgetLocalDataSource> {
        getBudgetLocalDataSource(appDatabase = get())
    }

    single<BudgetRemoteDataSource> {
        BudgetRemoteDataSourceImpl(
            client = get {
                parametersOf("budget")
            }
        )
    }

    single<BudgetOnWidgetLocalDataSource> {
        getBudgetOnWidgetLocalDataSource(appDatabase = get())
    }

    single<BudgetOnWidgetRemoteDataSource> {
        BudgetOnWidgetRemoteDataSourceImpl(
            client = get {
                parametersOf("budget")
            }
        )
    }

    /* ---------- Repositories ---------- */

    single<BudgetRepository> {
        BudgetRepositoryImpl(localSource = get(), remoteSource = get(), syncHelper = get())
    }

    single<BudgetOnWidgetRepository> {
        BudgetOnWidgetRepositoryImpl(localSource = get(), remoteSource = get(), syncHelper = get())
    }

    /* ---------- Use Cases ---------- */

    single<SaveBudgetsAndDeleteRestUseCase> {
        SaveBudgetsAndDeleteRestUseCaseImpl(budgetRepository = get())
    }

    single<SaveBudgetsOnWidgetAndDeleteRestUseCase> {
        SaveBudgetsOnWidgetAndDeleteRestUseCaseImpl(budgetOnWidgetRepository = get())
    }

    single<GetBudgetUseCase> {
        GetBudgetUseCaseImpl(budgetRepository = get())
    }

    single<GetBudgetsUseCase> {
        GetBudgetsUseCaseImpl(budgetsRepository = get())
    }

    single<GetGroupedBudgetsUseCase> {
        GetGroupedBudgetsUseCaseImpl(budgetRepository = get())
    }

    single<GetGroupedFilledBudgetsUseCase> {
        GetGroupedFilledBudgetsUseCaseImpl(
            budgetRepository = get(),
            getTransactionsInDateRangeUseCase = get()
        )
    }

    single<GetBudgetIdsOnWidgetUseCase> {
        GetBudgetIdsOnWidgetUseCaseImpl(budgetOnWidgetRepository = get())
    }

    single<GetBudgetsOnWidgetUseCase> {
        GetBudgetsOnWidgetUseCaseImpl(
            budgetRepository = get(),
            getBudgetIdsOnWidgetUseCase = get(),
            getTransactionsInDateRangeUseCase = get()
        )
    }

    /* ---------- ViewModels ---------- */

    viewModel {
        BudgetsViewModel(
            getGroupedFilledBudgetsUseCase = get(),
            getAccountsUseCase = get(),
            getExpenseCategoriesGroupedUseCase = get()
        )
    }

    viewModel { parameters ->
        BudgetStatisticsViewModel(
            budgetId = parameters.get(),
            getAccountsUseCase = get(),
            getExpenseCategoriesGroupedUseCase = get(),
            getBudgetUseCase = get(),
            getTotalExpensesInDateRangesUseCase = get(),
            resourceManager = get { parametersOf(parameters.get<String>()) }
        )
    }

    viewModel {
        BudgetsOnWidgetViewModel(
            getBudgetsOnWidgetUseCase = get(),
            getAccountsUseCase = get(),
            getExpenseCategoriesGroupedUseCase = get()
        )
    }

    viewModel {
        BudgetsOnWidgetSettingsViewModel(
            saveBudgetsOnWidgetAndDeleteRestUseCase = get(),
            getBudgetIdsOnWidgetUseCase = get(),
            getGroupedBudgetsUseCase = get(),
            getAccountsUseCase = get(),
            getExpenseCategoriesGroupedUseCase = get()
        )
    }

    viewModel {
        EditBudgetsViewModel(
            getAccountsUseCase = get(),
            getExpenseCategoriesGroupedUseCase = get(),
            getBudgetsUseCase = get(),
            saveBudgetsAndDeleteRestUseCase = get(),
            changeAppSetupStageUseCase = get()
        )
    }

    viewModel {
        EditBudgetViewModel(
            getAccountsUseCase = get(),
            getCategoriesGroupedUseCase = get()
        )
    }

}