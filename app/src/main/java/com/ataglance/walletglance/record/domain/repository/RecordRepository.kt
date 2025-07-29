package com.ataglance.walletglance.record.domain.repository

import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.transaction.domain.model.RecordWithItems
import kotlinx.coroutines.flow.Flow

interface RecordRepository {

    suspend fun upsertRecordWithItems(recordWithItems: RecordWithItems)

    suspend fun upsertRecordsWithItems(recordsWithItems: List<RecordWithItems>)

    suspend fun deleteRecordWithItems(recordWithItems: RecordWithItems)

    suspend fun deleteAndUpsertRecordWithItems(
        recordWithItemsToDelete: RecordWithItems,
        recordWithItemsToUpsert: RecordWithItems
    )

    suspend fun getRecordWithItems(id: Long): RecordWithItems?

    suspend fun getLastRecordWithItemsByTypeAndAccount(type: Char, accountId: Int): RecordWithItems?

    fun getRecordsWithItemsInDateRangeAsFlow(from: Long, to: Long): Flow<List<RecordWithItems>>

    suspend fun getRecordsWithItemsInDateRange(from: Long, to: Long): List<RecordWithItems>

    suspend fun getTotalExpensesInDateRangeByAccountsAndCategory(
        dateRange: TimestampRange,
        accountIds: List<Int>,
        categoryId: Int
    ): Double

}