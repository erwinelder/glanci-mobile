package com.ataglance.walletglance.di

import com.ataglance.walletglance.personalization.data.local.source.WidgetLocalDataSource
import com.ataglance.walletglance.personalization.data.local.source.getWidgetLocalDataSource
import com.ataglance.walletglance.personalization.data.remote.source.WidgetRemoteDataSource
import com.ataglance.walletglance.personalization.data.remote.source.WidgetRemoteDataSourceImpl
import com.ataglance.walletglance.personalization.domain.repository.WidgetRepository
import com.ataglance.walletglance.personalization.data.repository.WidgetRepositoryImpl
import com.ataglance.walletglance.personalization.domain.usecase.widgets.GetWidgetsUseCase
import com.ataglance.walletglance.personalization.domain.usecase.widgets.GetWidgetsUseCaseImpl
import com.ataglance.walletglance.personalization.domain.usecase.widgets.SaveWidgetsAndDeleteRestUseCase
import com.ataglance.walletglance.personalization.domain.usecase.widgets.SaveWidgetsAndDeleteRestUseCaseImpl
import com.ataglance.walletglance.personalization.presentation.viewmodel.PersonalizationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val personalizationModule = module {

    /* ---------- Data Sources ---------- */

    single<WidgetLocalDataSource> {
        getWidgetLocalDataSource(appDatabase = get())
    }

    single<WidgetRemoteDataSource> {
        WidgetRemoteDataSourceImpl(
            client = get {
                parametersOf("personalization")
            }
        )
    }

    /* ---------- Repositories ---------- */

    single<WidgetRepository> {
        WidgetRepositoryImpl(localSource = get(), remoteSource = get(), syncHelper = get())
    }

    /* ---------- Use Cases ---------- */

    single<SaveWidgetsAndDeleteRestUseCase> {
        SaveWidgetsAndDeleteRestUseCaseImpl(widgetRepository = get())
    }

    single<GetWidgetsUseCase> {
        GetWidgetsUseCaseImpl(widgetRepository = get())
    }

    /* ---------- ViewModels ---------- */

    viewModel {
        PersonalizationViewModel(
            getAppThemeConfigurationUseCase = get(),
            changeAppLookPreferencesUseCase = get(),
            saveWidgetsAndDeleteRestUseCase = get(),
            getWidgetsUseCase = get(),
            saveNavigationButtonsUseCase = get(),
            getNavigationButtonScreensUseCase = get(),
            userContext = get()
        )
    }

}