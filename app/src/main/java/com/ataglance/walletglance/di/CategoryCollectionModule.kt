package com.ataglance.walletglance.di

import com.ataglance.walletglance.categoryCollection.data.local.source.CategoryCollectionLocalDataSource
import com.ataglance.walletglance.categoryCollection.data.local.source.getCategoryCollectionLocalDataSource
import com.ataglance.walletglance.categoryCollection.data.remote.source.CategoryCollectionRemoteDataSource
import com.ataglance.walletglance.categoryCollection.data.remote.source.CategoryCollectionRemoteDataSourceImpl
import com.ataglance.walletglance.categoryCollection.domain.repository.CategoryCollectionRepository
import com.ataglance.walletglance.categoryCollection.data.repository.CategoryCollectionRepositoryImpl
import com.ataglance.walletglance.categoryCollection.domain.usecase.GetCategoryCollectionsGroupedUseCase
import com.ataglance.walletglance.categoryCollection.domain.usecase.GetCategoryCollectionsGroupedUseCaseImpl
import com.ataglance.walletglance.categoryCollection.domain.usecase.SaveCategoryCollectionsAndDeleteRestUseCase
import com.ataglance.walletglance.categoryCollection.domain.usecase.SaveCategoryCollectionsAndDeleteRestUseCaseImpl
import com.ataglance.walletglance.categoryCollection.presentation.viewmodel.EditCategoryCollectionViewModel
import com.ataglance.walletglance.categoryCollection.presentation.viewmodel.EditCategoryCollectionsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val categoryCollectionModule = module {

    /* ---------- Data Sources ---------- */

    single<CategoryCollectionLocalDataSource> {
        getCategoryCollectionLocalDataSource(appDatabase = get())
    }

    single<CategoryCollectionRemoteDataSource> {
        CategoryCollectionRemoteDataSourceImpl(
            client = get {
                parametersOf("categoryCollection")
            }
        )
    }

    /* ---------- Repositories ---------- */

    single<CategoryCollectionRepository> {
        CategoryCollectionRepositoryImpl(
            localSource = get(),
            remoteSource = get(),
            syncHelper = get()
        )
    }

    /* ---------- Use Cases ---------- */

    single<SaveCategoryCollectionsAndDeleteRestUseCase> {
        SaveCategoryCollectionsAndDeleteRestUseCaseImpl(categoryCollectionRepository = get())
    }

    single<GetCategoryCollectionsGroupedUseCase> {
        GetCategoryCollectionsGroupedUseCaseImpl(categoryCollectionRepository = get())
    }

    /* ---------- ViewModels ---------- */

    viewModel {
        EditCategoryCollectionsViewModel(
            saveCategoryCollectionsAndDeleteRestUseCase = get(),
            getCategoryCollectionsGroupedUseCase = get(),
            getAllCategoriesUseCase = get()
        )
    }

    viewModel {
        EditCategoryCollectionViewModel(getCategoriesGroupedUseCase = get())
    }

}