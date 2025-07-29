package com.ataglance.walletglance.transfer.data.remote.source

import com.glanci.request.shared.ResultData
import com.glanci.request.shared.SimpleResult
import com.glanci.request.shared.error.DataError
import com.glanci.request.shared.error.TransferDataError
import com.glanci.transfer.shared.dto.TransferCommandDto
import com.glanci.transfer.shared.dto.TransferQueryDto
import com.glanci.transfer.shared.service.TransferService
import kotlinx.rpc.krpc.ktor.client.KtorRpcClient
import kotlinx.rpc.withService

class TransferRemoteDataSourceImpl(
    private val service: TransferService
) : TransferRemoteDataSource {

    constructor(client: KtorRpcClient) : this(service = client.withService<TransferService>())


    override suspend fun getUpdateTime(token: String): ResultData<Long, DataError> {
        return runCatching {
            service.getUpdateTime(token = token)
        }.getOrDefault(
            defaultValue = ResultData.Error(error = TransferDataError.TransferError)
        )
    }

    override suspend fun synchronizeTransfers(
        transfers: List<TransferCommandDto>,
        timestamp: Long,
        token: String
    ): SimpleResult<DataError> {
        return runCatching {
            service.saveTransfers(transfers = transfers, timestamp = timestamp, token = token)
        }.getOrDefault(
            defaultValue = SimpleResult.Error(error = TransferDataError.TransferError)
        )
    }

    override suspend fun getTransfersAfterTimestamp(
        timestamp: Long,
        token: String
    ): ResultData<List<TransferQueryDto>, DataError> {
        return runCatching {
            service.getTransfersAfterTimestamp(timestamp = timestamp, token = token)
        }.getOrDefault(
            defaultValue = ResultData.Error(error = TransferDataError.TransferError)
        )
    }

    override suspend fun synchronizeTransfersAndGetAfterTimestamp(
        transfers: List<TransferCommandDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): ResultData<List<TransferQueryDto>, DataError> {
        return runCatching {
            service.saveTransfersAndGetAfterTimestamp(
                transfers = transfers,
                timestamp = timestamp,
                localTimestamp = localTimestamp,
                token = token
            )
        }.getOrDefault(
            defaultValue = ResultData.Error(error = TransferDataError.TransferError)
        )
    }

}