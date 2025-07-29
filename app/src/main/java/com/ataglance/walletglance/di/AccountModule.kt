package com.ataglance.walletglance.di

import com.ataglance.walletglance.account.data.local.source.AccountLocalDataSource
import com.ataglance.walletglance.account.data.local.source.getAccountLocalDataSource
import com.ataglance.walletglance.account.data.remote.source.AccountRemoteDataSource
import com.ataglance.walletglance.account.data.remote.source.AccountRemoteDataSourceImpl
import com.ataglance.walletglance.account.data.repository.AccountRepositoryImpl
import com.ataglance.walletglance.account.domain.repository.AccountRepository
import com.ataglance.walletglance.account.domain.usecase.ApplyEditedRecordToAccountsUseCase
import com.ataglance.walletglance.account.domain.usecase.ApplyEditedRecordToAccountsUseCaseImpl
import com.ataglance.walletglance.account.domain.usecase.ApplyEditedTransferToAccountsUseCase
import com.ataglance.walletglance.account.domain.usecase.ApplyEditedTransferToAccountsUseCaseImpl
import com.ataglance.walletglance.account.domain.usecase.ApplyNewRecordToAccountUseCase
import com.ataglance.walletglance.account.domain.usecase.ApplyNewRecordToAccountUseCaseImpl
import com.ataglance.walletglance.account.domain.usecase.ApplyNewTransferToAccountsUseCase
import com.ataglance.walletglance.account.domain.usecase.ApplyNewTransferToAccountsUseCaseImpl
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCaseImpl
import com.ataglance.walletglance.account.domain.usecase.RollbackRecordToAccountUseCase
import com.ataglance.walletglance.account.domain.usecase.RollbackRecordToAccountUseCaseImpl
import com.ataglance.walletglance.account.domain.usecase.RollbackTransferToAccountsUseCase
import com.ataglance.walletglance.account.domain.usecase.RollbackTransferToAccountsUseCaseImpl
import com.ataglance.walletglance.account.domain.usecase.SaveAccountsAndDeleteRestUseCase
import com.ataglance.walletglance.account.domain.usecase.SaveAccountsAndDeleteRestUseCaseImpl
import com.ataglance.walletglance.account.presentation.viewmodel.ActiveAccountCardViewModel
import com.ataglance.walletglance.account.presentation.viewmodel.CurrencyPickerViewModel
import com.ataglance.walletglance.account.presentation.viewmodel.EditAccountsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val accountModule = module {

    /* ---------- Data Sources ---------- */

    single<AccountLocalDataSource> {
        getAccountLocalDataSource(appDatabase = get())
    }

    single<AccountRemoteDataSource> {
        AccountRemoteDataSourceImpl(
            client = get {
                parametersOf("account")
            }
        )
    }

    /* ---------- Repositories ---------- */

    single<AccountRepository> {
        AccountRepositoryImpl(
            localSource = get(),
            remoteSource = get(),
            syncHelper = get()
        )
    }

    /* ---------- Use Cases ---------- */

    single<SaveAccountsAndDeleteRestUseCase> {
        SaveAccountsAndDeleteRestUseCaseImpl(
            accountRepository = get(),
            transformAccountTransactionsToRecords = get()
        )
    }

    single<ApplyNewRecordToAccountUseCase> {
        ApplyNewRecordToAccountUseCaseImpl(accountRepository = get())
    }

    single<ApplyEditedRecordToAccountsUseCase> {
        ApplyEditedRecordToAccountsUseCaseImpl(accountRepository = get())
    }

    single<RollbackRecordToAccountUseCase> {
        RollbackRecordToAccountUseCaseImpl(accountRepository = get())
    }

    single<ApplyNewTransferToAccountsUseCase> {
        ApplyNewTransferToAccountsUseCaseImpl(accountRepository = get())
    }

    single<ApplyEditedTransferToAccountsUseCase> {
        ApplyEditedTransferToAccountsUseCaseImpl(accountRepository = get())
    }

    single<RollbackTransferToAccountsUseCase> {
        RollbackTransferToAccountsUseCaseImpl(accountRepository = get())
    }

    single<GetAccountsUseCase> {
        GetAccountsUseCaseImpl(accountRepository = get())
    }

    /* ---------- ViewModels ---------- */

    viewModel {
        EditAccountsViewModel(
            saveAccountsAndDeleteRestUseCase = get(),
            getAccountsUseCase = get()
        )
    }

    viewModel { params ->
        CurrencyPickerViewModel(selectedCurrency = params.getOrNull<String>())
    }

    viewModel {
        ActiveAccountCardViewModel(getTodayTotalExpensesForAccountUseCase = get())
    }

}