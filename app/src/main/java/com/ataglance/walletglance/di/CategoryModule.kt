package com.ataglance.walletglance.di

import com.ataglance.walletglance.category.data.local.source.CategoryLocalDataSource
import com.ataglance.walletglance.category.data.local.source.getCategoryLocalDataSource
import com.ataglance.walletglance.category.data.remote.source.CategoryRemoteDataSource
import com.ataglance.walletglance.category.data.remote.source.CategoryRemoteDataSourceImpl
import com.ataglance.walletglance.category.data.repository.CategoryRepositoryImpl
import com.ataglance.walletglance.category.domain.repository.CategoryRepository
import com.ataglance.walletglance.category.domain.usecase.GetAllCategoriesUseCase
import com.ataglance.walletglance.category.domain.usecase.GetAllCategoriesUseCaseImpl
import com.ataglance.walletglance.category.domain.usecase.GetCategoriesGroupedUseCase
import com.ataglance.walletglance.category.domain.usecase.GetCategoriesGroupedUseCaseImpl
import com.ataglance.walletglance.category.domain.usecase.GetExpenseCategoriesGroupedUseCase
import com.ataglance.walletglance.category.domain.usecase.GetExpenseCategoriesGroupedUseCaseImpl
import com.ataglance.walletglance.category.domain.usecase.SaveCategoriesAndDeleteRestUseCase
import com.ataglance.walletglance.category.domain.usecase.SaveCategoriesAndDeleteRestUseCaseImpl
import com.ataglance.walletglance.category.domain.usecase.TranslateCategoriesUseCase
import com.ataglance.walletglance.category.domain.usecase.TranslateCategoriesUseCaseImpl
import com.ataglance.walletglance.category.presentation.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.presentation.viewmodel.CategoryStatisticsViewModel
import com.ataglance.walletglance.category.presentation.viewmodel.CategoryStatisticsWidgetViewModel
import com.ataglance.walletglance.category.presentation.viewmodel.EditCategoriesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val categoryModule = module {

    /* ---------- Data Sources ---------- */

    single<CategoryLocalDataSource> {
        getCategoryLocalDataSource(appDatabase = get())
    }

    single<CategoryRemoteDataSource> {
        CategoryRemoteDataSourceImpl(
            client = get {
                parametersOf("category")
            }
        )
    }

    /* ---------- Repositories ---------- */

    single<CategoryRepository> {
        CategoryRepositoryImpl(localSource = get(), remoteSource = get(), syncHelper = get())
    }

    /* ---------- Use Cases ---------- */

    single<SaveCategoriesAndDeleteRestUseCase> {
        SaveCategoriesAndDeleteRestUseCaseImpl(categoryRepository = get())
    }

    single<TranslateCategoriesUseCase> {
        TranslateCategoriesUseCaseImpl(categoryRepository = get())
    }

    single<GetAllCategoriesUseCase> {
        GetAllCategoriesUseCaseImpl(categoryRepository = get())
    }

    single<GetCategoriesGroupedUseCase> {
        GetCategoriesGroupedUseCaseImpl(categoryRepository = get())
    }

    single<GetExpenseCategoriesGroupedUseCase> {
        GetExpenseCategoriesGroupedUseCaseImpl(categoryRepository = get())
    }

    /* ---------- ViewModels ---------- */

    viewModel { parameters ->
        CategoryStatisticsViewModel(
            initialCategoryId = parameters.getOrNull(),
            initialCategoryType = parameters.get(),
            activeAccount = parameters.get(),
            activeDateRange = parameters.get(),
            defaultCollectionName = parameters.get(),
            getCategoriesGroupedUseCase = get(),
            getCategoryCollectionsGroupedUseCase = get(),
            getTransactionsInDateRangeUseCase = get()
        )
    }

    viewModel { parameters ->
        CategoryStatisticsWidgetViewModel(
            activeAccount = parameters.getOrNull(),
            activeDateRange = parameters.get(),
            getCategoriesGroupedUseCase = get(),
            getTransactionsInDateRangeUseCase = get()
        )
    }

    viewModel { parameters ->
        EditCategoriesViewModel(
            defaultCategoriesPackage = DefaultCategoriesPackage(
                resourceManager = get { parametersOf(parameters.get<String>()) }
            ),
            saveCategoriesAndDeleteRestUseCase = get(),
            getCategoriesGroupedUseCase = get()
        )
    }

}