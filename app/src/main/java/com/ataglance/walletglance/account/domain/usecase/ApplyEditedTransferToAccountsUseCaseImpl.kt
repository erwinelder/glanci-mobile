package com.ataglance.walletglance.account.domain.usecase

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.repository.AccountRepository
import com.ataglance.walletglance.transaction.domain.model.Transfer

class ApplyEditedTransferToAccountsUseCaseImpl(
    private val accountRepository: AccountRepository
) : ApplyEditedTransferToAccountsUseCase {

    override suspend fun execute(newTransfer: Transfer, currentTransfer: Transfer): Boolean {
        val accountsMap = mutableMapOf<Int, Account>()

        accountRepository.getAccount(id = currentTransfer.senderAccountId)?.let { account ->
            accountsMap[currentTransfer.senderAccountId] = account
                .addToBalance(amount = currentTransfer.senderAmount)
        } ?: return false
        accountRepository.getAccount(id = currentTransfer.receiverAccountId)?.let { account ->
            accountsMap[currentTransfer.receiverAccountId] = account
                .subtractFromBalance(amount = currentTransfer.receiverAmount)
        } ?: return false

        val senderAccount = accountsMap[newTransfer.senderAccountId]
            ?: accountRepository.getAccount(id = newTransfer.senderAccountId)
            ?: return false
        accountsMap[newTransfer.senderAccountId] = senderAccount
            .subtractFromBalance(amount = newTransfer.senderAmount)

        val receiverAccount = accountsMap[newTransfer.receiverAccountId]
            ?: accountRepository.getAccount(id = newTransfer.receiverAccountId)
            ?: return false
        accountsMap[newTransfer.receiverAccountId] = receiverAccount
            .addToBalance(amount = newTransfer.receiverAmount)

        val accounts = accountsMap.values.toList()

        accountRepository.upsertAccounts(accounts = accounts)

        return true
    }

}