package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.record.domain.repository.RecordRepository
import com.ataglance.walletglance.transaction.domain.model.RecordWithItems

class GetRecordUseCaseImpl(
    private val recordRepository: RecordRepository
) : GetRecordUseCase {

    override suspend fun execute(id: Long): RecordWithItems? {
        return recordRepository.getRecordWithItems(id = id)
    }

}