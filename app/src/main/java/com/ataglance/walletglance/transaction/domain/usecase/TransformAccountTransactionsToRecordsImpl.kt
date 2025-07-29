package com.ataglance.walletglance.transaction.domain.usecase

import com.ataglance.walletglance.record.domain.repository.RecordRepository
import com.ataglance.walletglance.transaction.domain.mapper.receiverToNewRecordWithItems
import com.ataglance.walletglance.transaction.domain.mapper.senderToNewRecordWithItems
import com.ataglance.walletglance.transfer.domain.usecase.GetTransfersByAccountsUseCase

class TransformAccountTransactionsToRecordsImpl(
    private val getTransfersByAccountsUseCase: GetTransfersByAccountsUseCase,
    private val recordRepository: RecordRepository
) : TransformAccountTransactionsToRecords {

    override suspend fun execute(accountIds: List<Int>) {
        val recordsWithItems = getTransfersByAccountsUseCase.execute(accountIds = accountIds)
            .map { transfer ->
                if (transfer.senderAccountId in accountIds) {
                    transfer.receiverToNewRecordWithItems()
                } else {
                    transfer.senderToNewRecordWithItems()
                }
            }
        recordRepository.upsertRecordsWithItems(recordsWithItems = recordsWithItems)
    }

}