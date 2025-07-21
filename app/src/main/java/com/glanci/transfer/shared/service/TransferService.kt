package com.glanci.transfer.shared.service

import com.glanci.transfer.shared.dto.TransferCommandDto
import com.glanci.transfer.shared.dto.TransferQueryDto
import kotlinx.rpc.annotations.Rpc

@Rpc
interface TransferService {

    suspend fun getUpdateTime(token: String): Long?

    suspend fun saveTransfers(transfers: List<TransferCommandDto>, timestamp: Long, token: String)

    suspend fun getTransfersAfterTimestamp(timestamp: Long, token: String): List<TransferQueryDto>?

    suspend fun saveTransfersAndGetAfterTimestamp(
        transfers: List<TransferCommandDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): List<TransferQueryDto>?

}