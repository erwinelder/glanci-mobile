package com.glanci.record.shared.service

import com.glanci.record.shared.dto.RecordWithItemsCommandDto
import com.glanci.record.shared.dto.RecordWithItemsQueryDto
import kotlinx.rpc.annotations.Rpc

@Rpc
interface RecordService {

    suspend fun getUpdateTime(token: String): Long?

    suspend fun saveRecordsWithItems(recordsWithItems: List<RecordWithItemsCommandDto>, timestamp: Long, token: String)

    suspend fun getRecordsWithItemsAfterTimestamp(timestamp: Long, token: String): List<RecordWithItemsQueryDto>?

    suspend fun saveRecordsWithItemsAndGetAfterTimestamp(
        recordsWithItems: List<RecordWithItemsCommandDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): List<RecordWithItemsQueryDto>?

}