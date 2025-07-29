package com.ataglance.walletglance.transfer.domain.usecase

import com.ataglance.walletglance.account.domain.usecase.RollbackTransferToAccountsUseCase
import com.ataglance.walletglance.transfer.data.repository.TransferRepository
import com.ataglance.walletglance.transfer.mapper.toDataModel
import com.ataglance.walletglance.transfer.mapper.toDomainModel

class DeleteTransferUseCaseImpl(
    private val transferRepository: TransferRepository,
    private val rollbackTransferToAccountsUseCase: RollbackTransferToAccountsUseCase
) : DeleteTransferUseCase {

    override suspend fun execute(transferId: Long) {
        val transfer = transferRepository.getTransfer(id = transferId)?.toDomainModel() ?: return

        rollbackTransferToAccountsUseCase.execute(transfer = transfer).also { if (!it) return }

        transferRepository.deleteTransfer(transfer = transfer.toDataModel())
    }

}