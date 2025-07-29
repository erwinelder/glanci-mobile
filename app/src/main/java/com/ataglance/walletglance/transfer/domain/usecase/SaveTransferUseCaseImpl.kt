package com.ataglance.walletglance.transfer.domain.usecase

import com.ataglance.walletglance.account.domain.usecase.ApplyEditedTransferToAccountsUseCase
import com.ataglance.walletglance.account.domain.usecase.ApplyNewTransferToAccountsUseCase
import com.ataglance.walletglance.transaction.domain.model.Transfer
import com.ataglance.walletglance.transfer.domain.repository.TransferRepository

class SaveTransferUseCaseImpl(
    private val transferRepository: TransferRepository,
    private val applyNewTransferToAccountsUseCase: ApplyNewTransferToAccountsUseCase,
    private val applyEditedTransferToAccountsUseCase: ApplyEditedTransferToAccountsUseCase
) : SaveTransferUseCase {

    override suspend fun execute(transfer: Transfer) {
        if (transfer.isNew) {
            applyNewTransferToAccountsUseCase.execute(transfer = transfer).also { if (!it) return }
        } else {
            val currentTransfer = transferRepository.getTransfer(id = transfer.id) ?: return
            applyEditedTransferToAccountsUseCase.execute(
                newTransfer = transfer,
                currentTransfer = currentTransfer
            )
        }

        transferRepository.upsertTransfer(transfer = transfer)
    }

}