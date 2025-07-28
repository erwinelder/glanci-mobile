package com.ataglance.walletglance.record.data.repository

import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.core.utils.asList
import com.ataglance.walletglance.record.data.local.model.RecordEntityWithItems
import com.ataglance.walletglance.record.data.local.source.RecordLocalDataSource
import com.ataglance.walletglance.record.data.mapper.toCommandDtoWithItems
import com.ataglance.walletglance.record.data.mapper.toDataModelWithItems
import com.ataglance.walletglance.record.data.mapper.toEntityWithItems
import com.ataglance.walletglance.record.data.model.RecordWithItemsDataModel
import com.ataglance.walletglance.record.data.remote.source.RecordRemoteDataSource
import com.glanci.record.shared.dto.RecordWithItemsQueryDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class RecordRepositoryImpl(
    private val localSource: RecordLocalDataSource,
    private val remoteSource: RecordRemoteDataSource,
    private val syncHelper: DataSyncHelper
) : RecordRepository {

    private suspend fun synchronizeRecords() {
        syncHelper.synchronizeData(
            tableName = TableName.Record,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { token -> remoteSource.getUpdateTime(token = token) },
            localDataGetter = { timestamp ->
                localSource.getRecordsWithItemsAfterTimestamp(timestamp = timestamp)
            },
            remoteDataGetter = { timestamp, token ->
                remoteSource.getRecordsWithItemsAfterTimestamp(
                    timestamp = timestamp, token = token
                )
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndSaveRecordsWithItems(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            remoteSynchronizer = { data, timestamp, token ->
                remoteSource.synchronizeRecordsWithItems(
                    recordsWithItems = data, timestamp = timestamp, token = token
                )
            },
            entityDeletedPredicate = { it.deleted },
            entityToCommandDtoMapper = RecordEntityWithItems::toCommandDtoWithItems,
            queryDtoToEntityMapper = RecordWithItemsQueryDto::toEntityWithItems
        )
    }

    override suspend fun upsertRecordWithItems(recordWithItems: RecordWithItemsDataModel) {
        upsertRecordsWithItems(recordsWithItems = recordWithItems.asList())
    }

    override suspend fun upsertRecordsWithItems(recordsWithItems: List<RecordWithItemsDataModel>) {
        syncHelper.upsertData(
            tableName = TableName.Record,
            data = recordsWithItems,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { token -> remoteSource.getUpdateTime(token = token) },
            localSoftCommand = { entities, timestamp ->
                localSource.saveRecordsWithItems(
                    recordsWithItems = entities, timestamp = timestamp
                )
            },
            remoteSoftCommand = { dtos, timestamp, token ->
                remoteSource.synchronizeRecordsWithItems(
                    recordsWithItems = dtos, timestamp = timestamp, token = token
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getRecordsWithItemsAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, localTimestamp, token ->
                remoteSource.synchronizeRecordsWithItemsAndGetAfterTimestamp(
                    recordsWithItems = dtos,
                    timestamp = timestamp,
                    localTimestamp = localTimestamp,
                    token = token
                )
            },
            dataModelToEntityMapper = RecordWithItemsDataModel::toEntityWithItems,
            entityToCommandDtoMapper = RecordEntityWithItems::toCommandDtoWithItems,
            queryDtoToEntityMapper = RecordWithItemsQueryDto::toEntityWithItems
        )
    }

    override suspend fun deleteRecordWithItems(recordWithItems: RecordWithItemsDataModel) {
        syncHelper.deleteData(
            tableName = TableName.Record,
            data = recordWithItems.asList(),
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { token -> remoteSource.getUpdateTime(token = token) },
            localSoftCommand = { entities, timestamp ->
                localSource.saveRecordsWithItems(
                    recordsWithItems = entities, timestamp = timestamp
                )
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndSaveRecordsWithItems(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            localDeleteCommand = { entities, timestamp ->
                localSource.deleteRecordsWithItems(
                    recordsWithItems = entities, timestamp = timestamp
                )
            },
            remoteSoftCommand = { dtos, timestamp, token ->
                remoteSource.synchronizeRecordsWithItems(
                    recordsWithItems = dtos, timestamp = timestamp, token = token
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getRecordsWithItemsAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, localTimestamp, token ->
                remoteSource.synchronizeRecordsWithItemsAndGetAfterTimestamp(
                    recordsWithItems = dtos,
                    timestamp = timestamp,
                    localTimestamp = localTimestamp,
                    token = token
                )
            },
            entityDeletedPredicate = { it.deleted },
            dataModelToEntityMapper = RecordWithItemsDataModel::toEntityWithItems,
            dataModelToCommandDtoMapper = RecordWithItemsDataModel::toCommandDtoWithItems,
            entityToCommandDtoMapper = RecordEntityWithItems::toCommandDtoWithItems,
            queryDtoToEntityMapper = RecordWithItemsQueryDto::toEntityWithItems
        )
    }

    override suspend fun deleteAndUpsertRecordWithItems(
        recordWithItemsToDelete: RecordWithItemsDataModel,
        recordWithItemsToUpsert: RecordWithItemsDataModel
    ) {
        syncHelper.deleteAndUpsertData(
            tableName = TableName.Record,
            toDelete = recordWithItemsToDelete.asList(),
            toUpsert = recordWithItemsToUpsert.asList(),
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { token -> remoteSource.getUpdateTime(token = token) },
            localSoftCommand = { entities, timestamp ->
                localSource.saveRecordsWithItems(
                    recordsWithItems = entities, timestamp = timestamp
                )
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndSaveRecordsWithItems(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            localDeleteCommand = { entities ->
                localSource.deleteRecordsWithItems(recordsWithItems = entities)
            },
            remoteSoftCommand = { dtos, timestamp, token ->
                remoteSource.synchronizeRecordsWithItems(
                    recordsWithItems = dtos, timestamp = timestamp, token = token
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getRecordsWithItemsAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, localTimestamp, token ->
                remoteSource.synchronizeRecordsWithItemsAndGetAfterTimestamp(
                    recordsWithItems = dtos,
                    timestamp = timestamp,
                    localTimestamp = localTimestamp,
                    token = token
                )
            },
            entityDeletedPredicate = { it.deleted },
            dataModelToEntityMapper = RecordWithItemsDataModel::toEntityWithItems,
            entityToCommandDtoMapper = RecordEntityWithItems::toCommandDtoWithItems,
            queryDtoToEntityMapper = RecordWithItemsQueryDto::toEntityWithItems
        )
    }

    override suspend fun getRecordWithItems(id: Long): RecordWithItemsDataModel? {
        synchronizeRecords()
        return localSource.getRecordWithItems(id = id)?.toDataModelWithItems()
    }

    override suspend fun getLastRecordWithItemsByTypeAndAccount(
        type: Char,
        accountId: Int
    ): RecordWithItemsDataModel? {
        synchronizeRecords()
        return localSource
            .getLastRecordWithItemsByTypeAndAccount(type = type, accountId = accountId)
            ?.toDataModelWithItems()
    }

    override fun getRecordsWithItemsInDateRangeAsFlow(
        from: Long,
        to: Long
    ): Flow<List<RecordWithItemsDataModel>> {
        return localSource.getRecordsWithItemsInDateRangeAsFlow(from = from, to = to)
            .onStart { synchronizeRecords() }
            .map { recordsWithItems ->
                recordsWithItems.map { it.toDataModelWithItems() }
            }
    }

    override suspend fun getRecordsWithItemsInDateRange(
        from: Long,
        to: Long
    ): List<RecordWithItemsDataModel> {
        synchronizeRecords()
        return localSource.getRecordsWithItemsInDateRange(from = from, to = to)
            .map { it.toDataModelWithItems() }
    }

    override suspend fun getTotalExpensesInDateRangeByAccountsAndCategory(
        dateRange: TimestampRange,
        accountIds: List<Int>,
        categoryId: Int
    ): Double {
        synchronizeRecords()
        return localSource.getTotalExpensesInDateRangeByAccountsAndCategory(
            from = dateRange.from,
            to = dateRange.to,
            accountIds = accountIds,
            categoryId = categoryId
        )
    }

}