package com.glanci.transfer.shared.service

import com.glanci.request.shared.ResultData
import com.glanci.request.shared.SimpleResult
import com.glanci.request.shared.error.DataError
import com.glanci.transfer.shared.dto.TransferCommandDto
import com.glanci.transfer.shared.dto.TransferQueryDto
import kotlinx.rpc.annotations.Rpc

@Rpc
interface TransferService {

    suspend fun getUpdateTime(token: String): ResultData<Long, DataError>

    suspend fun saveTransfers(
        transfers: List<TransferCommandDto>,
        timestamp: Long,
        token: String
    ): SimpleResult<DataError>

    suspend fun getTransfersAfterTimestamp(
        timestamp: Long,
        token: String
    ): ResultData<List<TransferQueryDto>, DataError>

    suspend fun saveTransfersAndGetAfterTimestamp(
        transfers: List<TransferCommandDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): ResultData<List<TransferQueryDto>, DataError>

}