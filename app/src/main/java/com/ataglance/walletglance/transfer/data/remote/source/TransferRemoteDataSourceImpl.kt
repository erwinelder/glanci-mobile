package com.ataglance.walletglance.transfer.data.remote.source

import android.util.Log
import com.glanci.transfer.shared.dto.TransferCommandDto
import com.glanci.transfer.shared.dto.TransferQueryDto
import com.glanci.transfer.shared.service.TransferService
import kotlinx.rpc.krpc.ktor.client.KtorRpcClient
import kotlinx.rpc.withService

class TransferRemoteDataSourceImpl(
    private val service: TransferService
) : TransferRemoteDataSource {

    constructor(client: KtorRpcClient) : this(service = client.withService<TransferService>())


    override suspend fun getUpdateTime(token: String): Long? {
        return runCatching {
            service.getUpdateTime(token = token)
        }.getOrNull().also { timestamp ->
            if (timestamp != null) {
                Log.d("TransferRemoteDataSourceImpl", "getUpdateTime:" +
                        "received update time $timestamp")
            } else {
                Log.w("TransferRemoteDataSourceImpl", "getUpdateTime:" +
                        "no update time received")
            }
        }
    }

    override suspend fun synchronizeTransfers(
        transfers: List<TransferCommandDto>,
        timestamp: Long,
        token: String
    ): Boolean {
        return runCatching {
            service.saveTransfers(transfers = transfers, timestamp = timestamp, token = token)
        }.isSuccess.also { success ->
            if (success) {
                Log.d("TransferRemoteDataSourceImpl", "synchronizeTransfers: " +
                        "synchronized ${transfers.size} transfers at timestamp $timestamp")
            } else {
                Log.e("TransferRemoteDataSourceImpl", "synchronizeTransfers: " +
                        "failed to synchronize ${transfers.size} transfers at timestamp $timestamp")
            }
        }
    }

    override suspend fun getTransfersAfterTimestamp(
        timestamp: Long,
        token: String
    ): List<TransferQueryDto>? {
        return runCatching {
            service.getTransfersAfterTimestamp(timestamp = timestamp, token = token)
        }.getOrNull().also { transfers ->
            transfers?.forEach {
                Log.d("TransferRemoteDataSourceImpl", "getTransfersAfterTimestamp:" +
                        "received transfer: id = ${it.id}")
            }
        }
    }

    override suspend fun synchronizeTransfersAndGetAfterTimestamp(
        transfers: List<TransferCommandDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): List<TransferQueryDto>? {
        return runCatching {
            service.saveTransfersAndGetAfterTimestamp(
                transfers = transfers,
                timestamp = timestamp,
                localTimestamp = localTimestamp,
                token = token
            )
        }.getOrNull().also { transfers ->
            Log.d("TransferRemoteDataSourceImpl", "synchronizeTransfersAndGetAfterTimestamp:" +
                    "synchronized ${transfers?.size ?: 0} transfers at timestamp $timestamp" +
                    " with local timestamp $localTimestamp")
            transfers?.forEach {
                Log.d("TransferRemoteDataSourceImpl", "synchronizeTransfersAndGetAfterTimestamp:" +
                        "received transfer: id = ${it.id}")
            }
        }
    }

}