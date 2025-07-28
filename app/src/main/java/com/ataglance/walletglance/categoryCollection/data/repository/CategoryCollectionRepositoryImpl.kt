package com.ataglance.walletglance.categoryCollection.data.repository

import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionEntityWithAssociations
import com.ataglance.walletglance.categoryCollection.data.local.source.CategoryCollectionLocalDataSource
import com.ataglance.walletglance.categoryCollection.data.mapper.toDataModel
import com.ataglance.walletglance.categoryCollection.data.mapper.toDataModelWithAssociations
import com.ataglance.walletglance.categoryCollection.data.mapper.toDtoWithAssociations
import com.ataglance.walletglance.categoryCollection.data.mapper.toEntityWithAssociations
import com.ataglance.walletglance.categoryCollection.data.mapper.withAssociations
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionDataModel
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionWithAssociationsDataModel
import com.ataglance.walletglance.categoryCollection.data.remote.source.CategoryCollectionRemoteDataSource
import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.TableName
import com.glanci.categoryCollection.shared.dto.CategoryCollectionWithAssociationsDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class CategoryCollectionRepositoryImpl(
    private val localSource: CategoryCollectionLocalDataSource,
    private val remoteSource: CategoryCollectionRemoteDataSource,
    private val syncHelper: DataSyncHelper
) : CategoryCollectionRepository {

    private suspend fun synchronizeCollections() {
        syncHelper.synchronizeData(
            tableName = TableName.CategoryCollection,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { token -> remoteSource.getUpdateTime(token = token) },
            localDataGetter = { timestamp ->
                localSource.getCollectionsWithAssociationsAfterTimestamp(timestamp = timestamp)
            },
            remoteDataGetter = { timestamp, token ->
                remoteSource.getCollectionsWithAssociationsAfterTimestamp(
                    timestamp = timestamp, token = token
                )
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndUpsertCollectionsWithAssociations(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            remoteSynchronizer = { data, timestamp, token ->
                remoteSource.synchronizeCollectionsWithAssociations(
                    collections = data, timestamp = timestamp, token = token
                )
            },
            entityDeletedPredicate = { it.deleted },
            entityToCommandDtoMapper = CategoryCollectionEntityWithAssociations::toDtoWithAssociations,
            queryDtoToEntityMapper = CategoryCollectionWithAssociationsDto::toEntityWithAssociations,
        )
    }

    override suspend fun deleteAllCollectionsLocally() {
        localSource.deleteAllCategoryCollections()
    }

    override suspend fun deleteAndUpsertCollectionsWithAssociations(
        toDelete: List<CategoryCollectionDataModel>,
        toUpsert: List<CategoryCollectionWithAssociationsDataModel>
    ) {
        syncHelper.deleteAndUpsertData(
            tableName = TableName.CategoryCollection,
            toDelete = toDelete.map { it.withAssociations() },
            toUpsert = toUpsert,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { token -> remoteSource.getUpdateTime(token = token) },
            localSoftCommand = { entities, timestamp ->
                localSource.upsertCollectionsWithAssociations(
                    collectionsWithAssociations = entities, timestamp = timestamp
                )
                entities
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndUpsertCollectionsWithAssociations(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            localDeleteCommand = { entities ->
                localSource.deleteCollectionsWithAssociations(collectionsWithAssociations = entities)
            },
            remoteSoftCommand = { dtos, timestamp, token ->
                remoteSource.synchronizeCollectionsWithAssociations(
                    collections = dtos, timestamp = timestamp, token = token
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getCollectionsWithAssociationsAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, localTimestamp, token ->
                remoteSource.synchronizeCollectionsWithAssociationsAndGetAfterTimestamp(
                    collections = dtos,
                    timestamp = timestamp,
                    localTimestamp = localTimestamp,
                    token = token
                )
            },
            entityDeletedPredicate = { it.deleted },
            dataModelToEntityMapper = CategoryCollectionWithAssociationsDataModel::toEntityWithAssociations,
            entityToCommandDtoMapper = CategoryCollectionEntityWithAssociations::toDtoWithAssociations,
            queryDtoToEntityMapper = CategoryCollectionWithAssociationsDto::toEntityWithAssociations
        )
    }

    override suspend fun getAllCollections(): List<CategoryCollectionDataModel> {
        synchronizeCollections()
        return localSource.getAllCollections().map { it.toDataModel() }
    }

    override fun getAllCollectionsWithAssociationsAsFlow(
    ): Flow<List<CategoryCollectionWithAssociationsDataModel>> {
        return localSource.getAllCollectionsWithAssociationsAsFlow()
            .onStart { synchronizeCollections() }
            .map { collectionsWithAssociations ->
                collectionsWithAssociations.map { it.toDataModelWithAssociations() }
            }
    }

    override suspend fun getAllCollectionsWithAssociations(
    ): List<CategoryCollectionWithAssociationsDataModel> {
        synchronizeCollections()
        return localSource.getAllCollectionsWithAssociations().map {
            it.toDataModelWithAssociations()
        }
    }

}