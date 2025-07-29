package com.ataglance.walletglance.categoryCollection.data.repository

import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionEntityWithAssociations
import com.ataglance.walletglance.categoryCollection.data.local.source.CategoryCollectionLocalDataSource
import com.ataglance.walletglance.categoryCollection.data.mapper.toDataModel
import com.ataglance.walletglance.categoryCollection.data.mapper.toDataModelWithAssociations
import com.ataglance.walletglance.categoryCollection.data.mapper.toDtoWithAssociations
import com.ataglance.walletglance.categoryCollection.data.mapper.toEntityWithAssociations
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionWithAssociationsDataModel
import com.ataglance.walletglance.categoryCollection.data.remote.source.CategoryCollectionRemoteDataSource
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollection
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithIds
import com.ataglance.walletglance.categoryCollection.domain.repository.CategoryCollectionRepository
import com.ataglance.walletglance.categoryCollection.mapper.toDataModelWithAssociations
import com.ataglance.walletglance.categoryCollection.mapper.toDomainModel
import com.ataglance.walletglance.categoryCollection.mapper.toDomainModelWithIds
import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.TableName
import com.glanci.categoryCollection.shared.dto.CategoryCollectionWithAssociationsDto
import com.glanci.request.shared.SimpleResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class CategoryCollectionRepositoryImpl(
    private val localSource: CategoryCollectionLocalDataSource,
    private val remoteSource: CategoryCollectionRemoteDataSource,
    private val syncHelper: DataSyncHelper
) : CategoryCollectionRepository {

    private suspend fun synchronizeCollections() {
        syncHelper.synchronizeDataSafe(
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
        ).also { result ->
            when (result) {
                is SimpleResult.Success -> println("Collections synchronized successfully.")
                is SimpleResult.Error -> println("Error synchronizing collections: ${result.error}")
            }
        }
    }

    override suspend fun deleteAllCollectionsLocally() {
        localSource.deleteAllCategoryCollections()
    }

    override suspend fun deleteAndUpsertCollectionsWithAssociations(
        toDelete: List<CategoryCollection>,
        toUpsert: List<CategoryCollectionWithIds>
    ) {
        val toDelete = toDelete.map { it.toDataModelWithAssociations() }
        val toUpsert = toUpsert.mapNotNull { it.toDataModelWithAssociations() }

        syncHelper.deleteAndUpsertDataSafe(
            tableName = TableName.CategoryCollection,
            toDelete = toDelete,
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
        ).also { result ->
            when (result) {
                is SimpleResult.Success -> println("Collections with associations deleted and upserted successfully.")
                is SimpleResult.Error -> println("Error deleting and upserting collections: ${result.error}")
            }
        }
    }

    override suspend fun getAllCollections(): List<CategoryCollection> {
        synchronizeCollections()
        return localSource.getAllCollections().map { it.toDataModel().toDomainModel() }
    }

    override fun getAllCollectionsWithAssociationsAsFlow(): Flow<List<CategoryCollectionWithIds>> {
        return localSource.getAllCollectionsWithAssociationsAsFlow()
            .onStart { synchronizeCollections() }
            .map { collectionsWithAssociations ->
                collectionsWithAssociations.map {
                    it.toDataModelWithAssociations().toDomainModelWithIds()
                }
            }
    }

    override suspend fun getAllCollectionsWithAssociations(): List<CategoryCollectionWithIds> {
        return localSource.getAllCollectionsWithAssociations().map {
            it.toDataModelWithAssociations().toDomainModelWithIds()
        }
    }

}