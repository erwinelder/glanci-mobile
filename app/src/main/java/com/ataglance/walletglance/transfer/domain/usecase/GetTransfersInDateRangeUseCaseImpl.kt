package com.ataglance.walletglance.transfer.domain.usecase

import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.transaction.domain.model.Transfer
import com.ataglance.walletglance.transfer.domain.repository.TransferRepository
import kotlinx.coroutines.flow.Flow

class GetTransfersInDateRangeUseCaseImpl(
    private val transferRepository: TransferRepository
) : GetTransfersInDateRangeUseCase {

    override fun getAsFlow(range: TimestampRange): Flow<List<Transfer>> {
        return transferRepository.getTransfersInDateRangeAsFlow(from = range.from, to = range.to)
    }

    override suspend fun get(range: TimestampRange): List<Transfer> {
        return transferRepository.getTransfersInDateRange(from = range.from, to = range.to)
    }

}