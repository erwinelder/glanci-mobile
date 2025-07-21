package com.ataglance.walletglance.record.data.remote.source

import android.util.Log
import com.glanci.record.shared.dto.RecordWithItemsCommandDto
import com.glanci.record.shared.dto.RecordWithItemsQueryDto
import com.glanci.record.shared.service.RecordService
import kotlinx.rpc.krpc.ktor.client.KtorRpcClient
import kotlinx.rpc.withService

class RecordRemoteDataSourceImpl(
    private val service: RecordService
) : RecordRemoteDataSource {

    constructor(client: KtorRpcClient) : this(service = client.withService<RecordService>())


    override suspend fun getUpdateTime(token: String): Long? {
        return runCatching {
            service.getUpdateTime(token = token)
        }.getOrNull().also { timestamp ->
            if (timestamp != null) {
                Log.d("RecordRemoteDataSourceImpl", "getUpdateTime:" +
                        "received update time $timestamp")
            } else {
                Log.w("RecordRemoteDataSourceImpl", "getUpdateTime:" +
                        "no update time received")
            }
        }
    }

    override suspend fun synchronizeRecordsWithItems(
        recordsWithItems: List<RecordWithItemsCommandDto>,
        timestamp: Long,
        token: String
    ): Boolean {
        return runCatching {
            service.saveRecordsWithItems(
                recordsWithItems = recordsWithItems, timestamp = timestamp, token = token
            )
        }.isSuccess.also { success ->
            if (success) {
                Log.d("RecordRemoteDataSourceImpl", "synchronizeRecordsWithItems: " +
                        "synchronized ${recordsWithItems.size} records at timestamp $timestamp")
            } else {
                Log.e("RecordRemoteDataSourceImpl", "synchronizeRecordsWithItems: " +
                        "failed to synchronize ${recordsWithItems.size} records at timestamp $timestamp")
            }
        }
    }

    override suspend fun getRecordsWithItemsAfterTimestamp(
        timestamp: Long,
        token: String
    ): List<RecordWithItemsQueryDto>? {
        return runCatching {
            service.getRecordsWithItemsAfterTimestamp(timestamp = timestamp, token = token)
        }.getOrNull().also { recordsWithItems ->
            recordsWithItems?.forEach {
                Log.d("RecordRemoteDataSourceImpl", "getRecordsWithItemsAfterTimestamp:" +
                        "received record with items: recordId = ${it.recordId}, items count: ${it.items.size}")
            }
        }
    }

    override suspend fun synchronizeRecordsWithItemsAndGetAfterTimestamp(
        recordsWithItems: List<RecordWithItemsCommandDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): List<RecordWithItemsQueryDto>? {
        return runCatching {
            service.saveRecordsWithItemsAndGetAfterTimestamp(
                recordsWithItems = recordsWithItems,
                timestamp = timestamp,
                localTimestamp = localTimestamp,
                token = token
            )
        }.getOrNull().also { recordsWithItems ->
            Log.d("RecordRemoteDataSourceImpl", "synchronizeRecordsWithItemsAndGetAfterTimestamp:" +
                    "synchronized ${recordsWithItems?.size ?: 0} records at timestamp $timestamp" +
                    " with local timestamp $localTimestamp")
            recordsWithItems?.forEach {
                Log.d("RecordRemoteDataSourceImpl", "synchronizeRecordsWithItemsAndGetAfterTimestamp:" +
                        "received record with items: recordId = ${it.recordId}, items count: ${it.items.size}")
            }
        }
    }

}