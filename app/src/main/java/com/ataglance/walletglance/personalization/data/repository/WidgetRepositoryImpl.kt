package com.ataglance.walletglance.personalization.data.repository

import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.personalization.data.local.model.WidgetEntity
import com.ataglance.walletglance.personalization.data.local.source.WidgetLocalDataSource
import com.ataglance.walletglance.personalization.data.mapper.toDataModel
import com.ataglance.walletglance.personalization.data.mapper.toDto
import com.ataglance.walletglance.personalization.data.mapper.toEntity
import com.ataglance.walletglance.personalization.data.model.WidgetDataModel
import com.ataglance.walletglance.personalization.data.remote.source.WidgetRemoteDataSource
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import com.ataglance.walletglance.personalization.domain.repository.WidgetRepository
import com.ataglance.walletglance.personalization.mapper.toDataModels
import com.ataglance.walletglance.personalization.mapper.toDomainModelsSorted
import com.glanci.personalization.shared.dto.WidgetDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class WidgetRepositoryImpl(
    private val localSource: WidgetLocalDataSource,
    private val remoteSource: WidgetRemoteDataSource,
    private val syncHelper: DataSyncHelper
) : WidgetRepository {

    private suspend fun synchronizeWidgets() {
        syncHelper.synchronizeDataSafe(
            tableName = TableName.Widget,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { token -> remoteSource.getUpdateTime(token = token) },
            localDataGetter = { timestamp ->
                localSource.getWidgetsAfterTimestamp(timestamp = timestamp)
            },
            remoteDataGetter = { timestamp, token ->
                remoteSource.getWidgetsAfterTimestamp(timestamp = timestamp, token = token)
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndUpsertWidgets(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            remoteSynchronizer = { data, timestamp, token ->
                remoteSource.synchronizeWidgets(
                    widgets = data, timestamp = timestamp, token = token
                )
            },
            entityDeletedPredicate = { it.deleted },
            entityToCommandDtoMapper = WidgetEntity::toDto,
            queryDtoToEntityMapper = WidgetDto::toEntity
        )
    }


    override suspend fun upsertWidgets(widgets: List<WidgetName>) {
        val widgets = widgets.toDataModels()

        syncHelper.upsertDataSafe(
            tableName = TableName.Widget,
            data = widgets,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { token -> remoteSource.getUpdateTime(token = token) },
            localSoftCommand = { entities, timestamp ->
                localSource.upsertWidgets(widgets = entities, timestamp = timestamp)
                entities
            },
            remoteSoftCommand = { dtos, timestamp, token ->
                remoteSource.synchronizeWidgets(
                    widgets = dtos, timestamp = timestamp, token = token
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getWidgetsAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, localTimestamp, token ->
                remoteSource.synchronizeWidgetsAndGetAfterTimestamp(
                    widgets = dtos,
                    timestamp = timestamp,
                    localTimestamp = localTimestamp,
                    token = token
                )
            },
            dataModelToEntityMapper = WidgetDataModel::toEntity,
            entityToCommandDtoMapper = WidgetEntity::toDto,
            queryDtoToEntityMapper = WidgetDto::toEntity
        )
    }

    override suspend fun deleteAndUpsertWidgets(
        toDelete: List<WidgetName>,
        toUpsert: List<WidgetName>
    ) {
        val toDelete = toDelete.toDataModels()
        val toUpsert = toUpsert.toDataModels()

        syncHelper.deleteAndUpsertDataSafe(
            tableName = TableName.Widget,
            toDelete = toDelete,
            toUpsert = toUpsert,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { token -> remoteSource.getUpdateTime(token = token) },
            localSoftCommand = { entities, timestamp ->
                localSource.upsertWidgets(widgets = entities, timestamp = timestamp)
                entities
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndUpsertWidgets(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            localDeleteCommand = { entities ->
                localSource.deleteWidgets(widgets = entities)
            },
            remoteSoftCommand = { dtos, timestamp, token ->
                remoteSource.synchronizeWidgets(
                    widgets = dtos, timestamp = timestamp, token = token
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getWidgetsAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, localTimestamp, token ->
                remoteSource.synchronizeWidgetsAndGetAfterTimestamp(
                    widgets = dtos,
                    timestamp = timestamp,
                    localTimestamp = localTimestamp,
                    token = token
                )
            },
            entityDeletedPredicate = { it.deleted },
            dataModelToEntityMapper = WidgetDataModel::toEntity,
            entityToCommandDtoMapper = WidgetEntity::toDto,
            queryDtoToEntityMapper = WidgetDto::toEntity
        )
    }

    override suspend fun deleteAllWidgetsLocally() {
        localSource.deleteAllWidgets()
    }

    override fun getAllWidgetsAsFlow(): Flow<List<WidgetName>> {
        return localSource
            .getAllWidgetsAsFlow()
            .onStart { synchronizeWidgets() }
            .map { widgets ->
                widgets.map { it.toDataModel() }.toDomainModelsSorted()
            }
    }

    override suspend fun getAllWidgets(): List<WidgetName> {
        synchronizeWidgets()
        return localSource.getAllWidgets().map { it.toDataModel() }.toDomainModelsSorted()
    }

}