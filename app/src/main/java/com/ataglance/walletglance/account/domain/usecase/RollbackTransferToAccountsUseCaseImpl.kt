package com.ataglance.walletglance.account.domain.usecase

import com.ataglance.walletglance.account.domain.repository.AccountRepository
import com.ataglance.walletglance.transaction.domain.model.Transfer

class RollbackTransferToAccountsUseCaseImpl(
    private val accountRepository: AccountRepository
) : RollbackTransferToAccountsUseCase {

    override suspend fun execute(transfer: Transfer): Boolean {
        val senderAccount = accountRepository.getAccount(id = transfer.senderAccountId)
            ?.addToBalance(amount = transfer.senderAmount)
            ?: return false
        val receiverAccount = accountRepository.getAccount(id = transfer.receiverAccountId)
            ?.subtractFromBalance(amount = transfer.receiverAmount)
            ?: return false
        val accounts = listOf(senderAccount, receiverAccount)

        accountRepository.upsertAccounts(accounts = accounts)

        return true
    }

}