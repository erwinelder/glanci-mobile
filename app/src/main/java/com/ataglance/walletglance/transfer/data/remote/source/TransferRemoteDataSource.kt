package com.ataglance.walletglance.transfer.data.remote.source

import com.glanci.request.shared.ResultData
import com.glanci.request.shared.SimpleResult
import com.glanci.request.shared.error.DataError
import com.glanci.transfer.shared.dto.TransferCommandDto
import com.glanci.transfer.shared.dto.TransferQueryDto

interface TransferRemoteDataSource {

    suspend fun getUpdateTime(token: String): ResultData<Long, DataError>

    suspend fun synchronizeTransfers(
        transfers: List<TransferCommandDto>,
        timestamp: Long,
        token: String
    ): SimpleResult<DataError>

    suspend fun getTransfersAfterTimestamp(
        timestamp: Long,
        token: String
    ): ResultData<List<TransferQueryDto>, DataError>

    suspend fun synchronizeTransfersAndGetAfterTimestamp(
        transfers: List<TransferCommandDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): ResultData<List<TransferQueryDto>, DataError>

}