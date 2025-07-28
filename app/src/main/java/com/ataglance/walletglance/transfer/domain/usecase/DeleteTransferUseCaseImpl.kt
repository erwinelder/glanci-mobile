package com.ataglance.walletglance.transfer.domain.usecase

import com.ataglance.walletglance.account.domain.repository.AccountRepository
import com.ataglance.walletglance.transfer.data.repository.TransferRepository

class DeleteTransferUseCaseImpl(
    private val transferRepository: TransferRepository,
    private val accountRepository: AccountRepository
) : DeleteTransferUseCase {

    override suspend fun execute(transferId: Long) {
        val transfer = transferRepository.getTransfer(id = transferId) ?: return

        val senderAccount = accountRepository.getAccount(id = transfer.senderAccountId)
            ?.addToBalance(amount = transfer.senderAmount)
            ?: return
        val receiverAccount = accountRepository.getAccount(id = transfer.receiverAccountId)
            ?.subtractFromBalance(amount = transfer.receiverAmount)
            ?: return
        val accounts = listOf(senderAccount, receiverAccount)

        accountRepository.upsertAccounts(accounts = accounts)
        transferRepository.deleteTransfer(transfer = transfer)
    }

}