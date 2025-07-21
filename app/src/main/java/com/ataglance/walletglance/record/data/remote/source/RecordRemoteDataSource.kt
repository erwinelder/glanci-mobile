package com.ataglance.walletglance.record.data.remote.source

import com.glanci.record.shared.dto.RecordWithItemsCommandDto
import com.glanci.record.shared.dto.RecordWithItemsQueryDto

interface RecordRemoteDataSource {

    suspend fun getUpdateTime(token: String): Long?

    suspend fun synchronizeRecordsWithItems(
        recordsWithItems: List<RecordWithItemsCommandDto>,
        timestamp: Long,
        token: String
    ): Boolean

    suspend fun getRecordsWithItemsAfterTimestamp(
        timestamp: Long,
        token: String
    ): List<RecordWithItemsQueryDto>?

    suspend fun synchronizeRecordsWithItemsAndGetAfterTimestamp(
        recordsWithItems: List<RecordWithItemsCommandDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): List<RecordWithItemsQueryDto>?

}