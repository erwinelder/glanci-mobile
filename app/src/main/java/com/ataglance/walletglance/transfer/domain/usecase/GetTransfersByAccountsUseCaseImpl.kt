package com.ataglance.walletglance.transfer.domain.usecase

import com.ataglance.walletglance.transaction.domain.model.Transfer
import com.ataglance.walletglance.transfer.domain.repository.TransferRepository

class GetTransfersByAccountsUseCaseImpl(
    private val transferRepository: TransferRepository
) : GetTransfersByAccountsUseCase {

    override suspend fun execute(accountIds: List<Int>): List<Transfer> {
        return transferRepository.getTransfersByAccounts(ids = accountIds)
    }

}