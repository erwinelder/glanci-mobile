package com.ataglance.walletglance.transfer.domain.usecase

import com.ataglance.walletglance.account.domain.usecase.RollbackTransferToAccountsUseCase
import com.ataglance.walletglance.transfer.domain.repository.TransferRepository

class DeleteTransferUseCaseImpl(
    private val transferRepository: TransferRepository,
    private val rollbackTransferToAccountsUseCase: RollbackTransferToAccountsUseCase
) : DeleteTransferUseCase {

    override suspend fun execute(transferId: Long) {
        val transfer = transferRepository.getTransfer(id = transferId) ?: return

        rollbackTransferToAccountsUseCase.execute(transfer = transfer).also { if (!it) return }

        transferRepository.deleteTransfer(transfer = transfer)
    }

}