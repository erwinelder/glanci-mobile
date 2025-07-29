package com.ataglance.walletglance.transfer.data.repository

import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.utils.asList
import com.ataglance.walletglance.transaction.domain.model.Transfer
import com.ataglance.walletglance.transfer.data.local.model.TransferEntity
import com.ataglance.walletglance.transfer.data.local.source.TransferLocalDataSource
import com.ataglance.walletglance.transfer.data.mapper.toCommandDto
import com.ataglance.walletglance.transfer.data.mapper.toDataModel
import com.ataglance.walletglance.transfer.data.mapper.toEntity
import com.ataglance.walletglance.transfer.data.model.TransferDataModel
import com.ataglance.walletglance.transfer.data.remote.source.TransferRemoteDataSource
import com.ataglance.walletglance.transfer.domain.repository.TransferRepository
import com.ataglance.walletglance.transfer.mapper.toDataModel
import com.ataglance.walletglance.transfer.mapper.toDomainModel
import com.glanci.request.shared.SimpleResult
import com.glanci.transfer.shared.dto.TransferQueryDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class TransferRepositoryImpl(
    private val localSource: TransferLocalDataSource,
    private val remoteSource: TransferRemoteDataSource,
    private val syncHelper: DataSyncHelper
) : TransferRepository {

    private suspend fun synchronizeTransfers() {
        syncHelper.synchronizeDataSafe(
            tableName = TableName.Transfer,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { token -> remoteSource.getUpdateTime(token = token) },
            localDataGetter = { timestamp ->
                localSource.getTransfersAfterTimestamp(timestamp = timestamp)
            },
            remoteDataGetter = { timestamp, token ->
                remoteSource.getTransfersAfterTimestamp(timestamp = timestamp, token = token)
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndSaveTransfers(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            remoteSynchronizer = { data, timestamp, token ->
                remoteSource.synchronizeTransfers(
                    transfers = data, timestamp = timestamp, token = token
                )
            },
            entityDeletedPredicate = { it.deleted },
            entityToCommandDtoMapper = TransferEntity::toCommandDto,
            queryDtoToEntityMapper = TransferQueryDto::toEntity
        ).also { result ->
            when (result) {
                is SimpleResult.Success -> println("Transfers synchronized successfully.")
                is SimpleResult.Error -> println("Error synchronizing transfers: ${result.error}")
            }
        }
    }

    override suspend fun upsertTransfer(transfer: Transfer) {
        val transfer = transfer.toDataModel()

        syncHelper.upsertDataSafe(
            tableName = TableName.Transfer,
            data = transfer.asList(),
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { token -> remoteSource.getUpdateTime(token = token) },
            localSoftCommand = { entities, timestamp ->
                localSource.saveTransfers(
                    transfers = entities, timestamp = timestamp
                )
            },
            remoteSoftCommand = { dtos, timestamp, token ->
                remoteSource.synchronizeTransfers(
                    transfers = dtos, timestamp = timestamp, token = token
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getTransfersAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, localTimestamp, token ->
                remoteSource.synchronizeTransfersAndGetAfterTimestamp(
                    transfers = dtos,
                    timestamp = timestamp,
                    localTimestamp = localTimestamp,
                    token = token
                )
            },
            dataModelToEntityMapper = TransferDataModel::toEntity,
            entityToCommandDtoMapper = TransferEntity::toCommandDto,
            queryDtoToEntityMapper = TransferQueryDto::toEntity
        ).also { result ->
            when (result) {
                is SimpleResult.Success -> println("Transfer upserted successfully.")
                is SimpleResult.Error -> println("Error upserting transfer: ${result.error}")
            }
        }
    }

    override suspend fun deleteTransfer(transfer: Transfer) {
        val transfer = transfer.toDataModel()

        syncHelper.deleteDataSafe(
            tableName = TableName.Transfer,
            data = transfer.asList(),
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { token -> remoteSource.getUpdateTime(token = token) },
            localSoftCommand = { entities, timestamp ->
                localSource.saveTransfers(
                    transfers = entities, timestamp = timestamp
                )
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndSaveTransfers(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            localDeleteCommand = { entities, timestamp ->
                localSource.deleteTransfers(transfers = entities, timestamp = timestamp)
            },
            remoteSoftCommand = { dtos, timestamp, token ->
                remoteSource.synchronizeTransfers(
                    transfers = dtos, timestamp = timestamp, token = token
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getTransfersAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, localTimestamp, token ->
                remoteSource.synchronizeTransfersAndGetAfterTimestamp(
                    transfers = dtos,
                    timestamp = timestamp,
                    localTimestamp = localTimestamp,
                    token = token
                )
            },
            entityDeletedPredicate = { it.deleted },
            dataModelToEntityMapper = TransferDataModel::toEntity,
            dataModelToCommandDtoMapper = TransferDataModel::toCommandDto,
            entityToCommandDtoMapper = TransferEntity::toCommandDto,
            queryDtoToEntityMapper = TransferQueryDto::toEntity
        ).also { result ->
            when (result) {
                is SimpleResult.Success -> println("Transfer deleted successfully.")
                is SimpleResult.Error -> println("Error deleting transfer: ${result.error}")
            }
        }
    }

    override suspend fun getTransfer(id: Long): Transfer? {
        synchronizeTransfers()
        return localSource.getTransfer(id = id)?.toDataModel()?.toDomainModel()
    }

    override fun getTransfersInDateRangeAsFlow(
        from: Long,
        to: Long
    ): Flow<List<Transfer>> {
        return localSource.getTransfersInDateRangeAsFlow(from = from, to = to)
            .onStart { synchronizeTransfers() }
            .map { transfers ->
                transfers.map { it.toDataModel().toDomainModel() }
            }
    }

    override suspend fun getTransfersInDateRange(
        from: Long,
        to: Long
    ): List<Transfer> {
        synchronizeTransfers()
        return localSource.getTransfersInDateRange(from = from, to = to).map {
            it.toDataModel().toDomainModel()
        }
    }

    override suspend fun getTransfersByAccounts(ids: List<Int>): List<Transfer> {
        synchronizeTransfers()
        return localSource.getTransfersByAccounts(accountIds = ids).map {
            it.toDataModel().toDomainModel()
        }
    }

    override suspend fun getTotalExpensesInDateRangeByAccounts(
        from: Long,
        to: Long,
        accountIds: List<Int>
    ): Double {
        synchronizeTransfers()
        return localSource.getTotalExpensesInDateRangeByAccounts(
            from = from, to = to, accountIds = accountIds
        )
    }

}