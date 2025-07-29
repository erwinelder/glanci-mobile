package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.account.domain.usecase.RollbackRecordToAccountUseCase
import com.ataglance.walletglance.record.domain.repository.RecordRepository

class DeleteRecordUseCaseImpl(
    private val recordRepository: RecordRepository,
    private val rollbackRecordToAccountUseCase: RollbackRecordToAccountUseCase
) : DeleteRecordUseCase {
    
    override suspend fun execute(id: Long) {
        val record = recordRepository.getRecordWithItems(id = id) ?: return

        rollbackRecordToAccountUseCase.execute(recordWithItems = record)
            .also { if (!it) return }

        recordRepository.deleteRecordWithItems(recordWithItems = record)
    }

}