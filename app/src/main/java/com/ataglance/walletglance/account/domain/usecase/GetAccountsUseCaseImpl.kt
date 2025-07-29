package com.ataglance.walletglance.account.domain.usecase

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class GetAccountsUseCaseImpl(
    private val accountRepository: AccountRepository
) : GetAccountsUseCase {

    override fun getAsFlow(): Flow<List<Account>> {
        return accountRepository.getAllAccountsAsFlow()
    }

    override suspend fun get(): List<Account> {
        return accountRepository.getAllAccounts()
    }

}