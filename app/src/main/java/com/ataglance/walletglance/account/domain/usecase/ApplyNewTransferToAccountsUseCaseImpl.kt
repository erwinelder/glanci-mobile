package com.ataglance.walletglance.account.domain.usecase

import com.ataglance.walletglance.account.domain.repository.AccountRepository
import com.ataglance.walletglance.transaction.domain.model.Transfer

class ApplyNewTransferToAccountsUseCaseImpl(
    private val accountRepository: AccountRepository
) : ApplyNewTransferToAccountsUseCase {

    override suspend fun execute(transfer: Transfer): Boolean {
        val senderAccount = accountRepository.getAccount(id = transfer.sender.accountId)
            ?.addToBalance(amount = transfer.sender.amount)
            ?: return false
        val receiverAccount = accountRepository.getAccount(id = transfer.receiver.accountId)
            ?.subtractFromBalance(amount = transfer.receiver.amount)
            ?: return false
        val accounts = listOf(senderAccount, receiverAccount)

        accountRepository.upsertAccounts(accounts = accounts)

        return true
    }

}