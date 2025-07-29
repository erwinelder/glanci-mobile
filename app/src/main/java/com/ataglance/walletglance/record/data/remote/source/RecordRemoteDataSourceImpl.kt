package com.ataglance.walletglance.record.data.remote.source

import com.glanci.record.shared.dto.RecordWithItemsCommandDto
import com.glanci.record.shared.dto.RecordWithItemsQueryDto
import com.glanci.record.shared.service.RecordService
import com.glanci.request.shared.ResultData
import com.glanci.request.shared.SimpleResult
import com.glanci.request.shared.error.DataError
import com.glanci.request.shared.error.RecordDataError
import kotlinx.rpc.krpc.ktor.client.KtorRpcClient
import kotlinx.rpc.withService

class RecordRemoteDataSourceImpl(
    private val service: RecordService
) : RecordRemoteDataSource {

    constructor(client: KtorRpcClient) : this(service = client.withService<RecordService>())


    override suspend fun getUpdateTime(token: String): ResultData<Long, DataError> {
        return runCatching {
            service.getUpdateTime(token = token)
        }.getOrDefault(
            defaultValue = ResultData.Error(error = RecordDataError.RecordError)
        )
    }

    override suspend fun synchronizeRecordsWithItems(
        recordsWithItems: List<RecordWithItemsCommandDto>,
        timestamp: Long,
        token: String
    ): SimpleResult<DataError> {
        return runCatching {
            service.saveRecordsWithItems(
                recordsWithItems = recordsWithItems, timestamp = timestamp, token = token
            )
        }.getOrDefault(
            defaultValue = SimpleResult.Error(error = RecordDataError.RecordError)
        )
    }

    override suspend fun getRecordsWithItemsAfterTimestamp(
        timestamp: Long,
        token: String
    ): ResultData<List<RecordWithItemsQueryDto>, DataError> {
        return runCatching {
            service.getRecordsWithItemsAfterTimestamp(timestamp = timestamp, token = token)
        }.getOrDefault(
            defaultValue = ResultData.Error(error = RecordDataError.RecordError)
        )
    }

    override suspend fun synchronizeRecordsWithItemsAndGetAfterTimestamp(
        recordsWithItems: List<RecordWithItemsCommandDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): ResultData<List<RecordWithItemsQueryDto>, DataError> {
        return runCatching {
            service.saveRecordsWithItemsAndGetAfterTimestamp(
                recordsWithItems = recordsWithItems,
                timestamp = timestamp,
                localTimestamp = localTimestamp,
                token = token
            )
        }.getOrDefault(
            defaultValue = ResultData.Error(error = RecordDataError.RecordError)
        )
    }

}