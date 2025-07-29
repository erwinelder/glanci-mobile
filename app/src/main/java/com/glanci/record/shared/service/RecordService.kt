package com.glanci.record.shared.service

import com.glanci.record.shared.dto.RecordWithItemsCommandDto
import com.glanci.record.shared.dto.RecordWithItemsQueryDto
import com.glanci.request.shared.ResultData
import com.glanci.request.shared.SimpleResult
import com.glanci.request.shared.error.DataError
import kotlinx.rpc.annotations.Rpc

@Rpc
interface RecordService {

    suspend fun getUpdateTime(token: String): ResultData<Long, DataError>

    suspend fun saveRecordsWithItems(
        recordsWithItems: List<RecordWithItemsCommandDto>,
        timestamp: Long,
        token: String
    ): SimpleResult<DataError>

    suspend fun getRecordsWithItemsAfterTimestamp(
        timestamp: Long,
        token: String
    ): ResultData<List<RecordWithItemsQueryDto>, DataError>

    suspend fun saveRecordsWithItemsAndGetAfterTimestamp(
        recordsWithItems: List<RecordWithItemsCommandDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): ResultData<List<RecordWithItemsQueryDto>, DataError>

}