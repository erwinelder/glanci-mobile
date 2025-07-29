package com.ataglance.walletglance.account.domain.usecase

import com.ataglance.walletglance.account.domain.repository.AccountRepository
import com.ataglance.walletglance.core.utils.asList
import com.ataglance.walletglance.transaction.domain.model.RecordWithItems

class RollbackRecordToAccountUseCaseImpl(
    private val accountRepository: AccountRepository
) : RollbackRecordToAccountUseCase {

    override suspend fun execute(
        recordWithItems: RecordWithItems
    ): Boolean {
        val account = accountRepository.getAccount(id = recordWithItems.accountId)
            ?.rollbackTransaction(amount = recordWithItems.totalAmount, type = recordWithItems.type)
            ?: return false

        accountRepository.upsertAccounts(accounts = account.asList())

        return true
    }

}