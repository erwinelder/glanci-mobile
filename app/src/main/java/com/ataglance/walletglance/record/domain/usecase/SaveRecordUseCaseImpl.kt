package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.account.domain.usecase.ApplyEditedRecordToAccountsUseCase
import com.ataglance.walletglance.account.domain.usecase.ApplyNewRecordToAccountUseCase
import com.ataglance.walletglance.record.domain.repository.RecordRepository
import com.ataglance.walletglance.transaction.domain.model.RecordWithItems

class SaveRecordUseCaseImpl(
    private val recordRepository: RecordRepository,
    private val getRecordUseCase: GetRecordUseCase,
    private val applyNewRecordToAccountUseCase: ApplyNewRecordToAccountUseCase,
    private val applyEditedRecordToAccountsUseCase: ApplyEditedRecordToAccountsUseCase
) : SaveRecordUseCase {

    override suspend fun execute(createdRecord: RecordWithItems) {
        if (createdRecord.isNew) {
            applyNewRecordToAccountUseCase.execute(recordWithItems = createdRecord)
                .also { if (!it) return }

            recordRepository.upsertRecordWithItems(recordWithItems = createdRecord)
        } else {
            val currentRecord = getRecordUseCase.execute(id = createdRecord.recordId) ?: return

            applyEditedRecordToAccountsUseCase
                .execute(createdRecord = createdRecord, currentRecord = currentRecord)
                .also { if (!it) return }

            recordRepository.deleteAndUpsertRecordWithItems(
                recordWithItemsToDelete = currentRecord, recordWithItemsToUpsert = createdRecord
            )
        }
    }

}