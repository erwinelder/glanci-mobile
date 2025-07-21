package com.ataglance.walletglance.record.data.repository

import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.record.data.model.RecordWithItemsDataModel
import kotlinx.coroutines.flow.Flow

interface RecordRepository {

    suspend fun upsertRecordWithItems(recordWithItems: RecordWithItemsDataModel)

    suspend fun upsertRecordsWithItems(recordsWithItems: List<RecordWithItemsDataModel>)

    suspend fun deleteRecordWithItems(recordWithItems: RecordWithItemsDataModel)

    suspend fun deleteAndUpsertRecordWithItems(
        recordWithItemsToDelete: RecordWithItemsDataModel,
        recordWithItemsToUpsert: RecordWithItemsDataModel
    )

    suspend fun getRecordWithItems(id: Long): RecordWithItemsDataModel?

    suspend fun getLastRecordWithItemsByTypeAndAccount(
        type: Char,
        accountId: Int
    ): RecordWithItemsDataModel?

    fun getRecordsWithItemsInDateRangeAsFlow(
        from: Long,
        to: Long
    ): Flow<List<RecordWithItemsDataModel>>

    suspend fun getRecordsWithItemsInDateRange(from: Long, to: Long): List<RecordWithItemsDataModel>

    suspend fun getTotalExpensesInDateRangeByAccountsAndCategory(
        dateRange: TimestampRange,
        accountIds: List<Int>,
        categoryId: Int
    ): Double

}