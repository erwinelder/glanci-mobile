package com.ataglance.walletglance.account.domain.usecase

import com.ataglance.walletglance.account.domain.repository.AccountRepository
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.core.utils.excludeItems
import com.ataglance.walletglance.transaction.domain.usecase.TransformAccountTransactionsToRecords

class SaveAccountsAndDeleteRestUseCaseImpl(
    private val accountRepository: AccountRepository,
    private val transformAccountTransactionsToRecords: TransformAccountTransactionsToRecords
) : SaveAccountsAndDeleteRestUseCase {

    override suspend fun execute(accounts: List<Account>) {
        val currentAccounts = accountRepository.getAllAccounts()

        val accountsToDelete = currentAccounts.excludeItems(accounts) { it.id }

        val accountIdsToDelete = accountsToDelete.map { it.id }
        if (accountsToDelete.isNotEmpty()) {
            transformAccountTransactionsToRecords.execute(accountIds = accountIdsToDelete)
        }

        accountRepository.deleteAndUpsertAccounts(
            toDelete = accountsToDelete, toUpsert = accounts
        )
    }

}