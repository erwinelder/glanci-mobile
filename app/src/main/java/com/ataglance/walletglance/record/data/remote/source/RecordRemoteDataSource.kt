package com.ataglance.walletglance.record.data.remote.source

import com.glanci.record.shared.dto.RecordWithItemsCommandDto
import com.glanci.record.shared.dto.RecordWithItemsQueryDto
import com.glanci.request.shared.ResultData
import com.glanci.request.shared.SimpleResult
import com.glanci.request.shared.error.DataError

interface RecordRemoteDataSource {

    suspend fun getUpdateTime(token: String): ResultData<Long, DataError>

    suspend fun synchronizeRecordsWithItems(
        recordsWithItems: List<RecordWithItemsCommandDto>,
        timestamp: Long,
        token: String
    ): SimpleResult<DataError>

    suspend fun getRecordsWithItemsAfterTimestamp(
        timestamp: Long,
        token: String
    ): ResultData<List<RecordWithItemsQueryDto>, DataError>

    suspend fun synchronizeRecordsWithItemsAndGetAfterTimestamp(
        recordsWithItems: List<RecordWithItemsCommandDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): ResultData<List<RecordWithItemsQueryDto>, DataError>

}