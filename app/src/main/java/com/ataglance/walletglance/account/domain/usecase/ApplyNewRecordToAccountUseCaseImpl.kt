package com.ataglance.walletglance.account.domain.usecase

import com.ataglance.walletglance.account.domain.repository.AccountRepository
import com.ataglance.walletglance.core.utils.asList
import com.ataglance.walletglance.transaction.domain.model.RecordWithItems

class ApplyNewRecordToAccountUseCaseImpl(
    private val accountRepository: AccountRepository
) : ApplyNewRecordToAccountUseCase {

    override suspend fun execute(recordWithItems: RecordWithItems): Boolean {
        val accounts = accountRepository
            .getAccount(id = recordWithItems.accountId)
            ?.applyTransaction(amount = recordWithItems.totalAmount, type = recordWithItems.type)
            ?.asList()
            ?: return false

        accountRepository.upsertAccounts(accounts = accounts)

        return true
    }

}