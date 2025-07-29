package com.ataglance.walletglance.transfer.domain.repository

import com.ataglance.walletglance.transaction.domain.model.Transfer
import kotlinx.coroutines.flow.Flow

interface TransferRepository {

    suspend fun upsertTransfer(transfer: Transfer)

    suspend fun deleteTransfer(transfer: Transfer)

    suspend fun getTransfer(id: Long): Transfer?

    fun getTransfersInDateRangeAsFlow(from: Long, to: Long): Flow<List<Transfer>>

    suspend fun getTransfersInDateRange(from: Long, to: Long): List<Transfer>

    suspend fun getTransfersByAccounts(ids: List<Int>): List<Transfer>

    suspend fun getTotalExpensesInDateRangeByAccounts(
        from: Long,
        to: Long,
        accountIds: List<Int>
    ): Double

}