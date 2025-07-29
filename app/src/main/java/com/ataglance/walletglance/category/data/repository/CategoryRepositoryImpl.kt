package com.ataglance.walletglance.category.data.repository

import com.ataglance.walletglance.category.data.local.model.CategoryEntity
import com.ataglance.walletglance.category.data.local.source.CategoryLocalDataSource
import com.ataglance.walletglance.category.data.mapper.toCommandDto
import com.ataglance.walletglance.category.data.mapper.toDataModel
import com.ataglance.walletglance.category.data.mapper.toEntity
import com.ataglance.walletglance.category.data.model.CategoryDataModel
import com.ataglance.walletglance.category.data.remote.source.CategoryRemoteDataSource
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.repository.CategoryRepository
import com.ataglance.walletglance.category.mapper.toDataModel
import com.ataglance.walletglance.category.mapper.toDomainModel
import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.TableName
import com.glanci.category.shared.dto.CategoryQueryDto
import com.glanci.request.shared.SimpleResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class CategoryRepositoryImpl(
    val localSource: CategoryLocalDataSource,
    val remoteSource: CategoryRemoteDataSource,
    private val syncHelper: DataSyncHelper
) : CategoryRepository {

    private suspend fun synchronizeCategories() {
        syncHelper.synchronizeDataSafe(
            tableName = TableName.Category,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { token -> remoteSource.getUpdateTime(token = token) },
            localDataGetter = { timestamp ->
                localSource.getCategoriesAfterTimestamp(timestamp = timestamp)
            },
            remoteDataGetter = { timestamp, token ->
                remoteSource.getCategoriesAfterTimestamp(timestamp = timestamp, token = token)
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndSaveCategories(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            remoteSynchronizer = { data, timestamp, token ->
                remoteSource.synchronizeCategories(
                    categories = data, timestamp = timestamp, token = token
                )
            },
            entityDeletedPredicate = { it.deleted },
            entityToCommandDtoMapper = CategoryEntity::toCommandDto,
            queryDtoToEntityMapper = CategoryQueryDto::toEntity
        ). also { result ->
            when (result) {
                is SimpleResult.Success -> println("Categories synchronized successfully.")
                is SimpleResult.Error -> println("Error synchronizing categories: ${result.error}")
            }
        }
    }

    override suspend fun upsertCategories(categories: List<Category>) {
        val categories = categories.map { it.toDataModel() }

        syncHelper.upsertDataSafe(
            tableName = TableName.Category,
            data = categories,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { token -> remoteSource.getUpdateTime(token = token) },
            localSoftCommand = { entities, timestamp ->
                localSource.saveCategories(categories = entities, timestamp = timestamp)
            },
            remoteSoftCommand = { dtos, timestamp, token ->
                remoteSource.synchronizeCategories(
                    categories = dtos, timestamp = timestamp, token = token
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getCategoriesAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, localTimestamp, token ->
                remoteSource.synchronizeCategoriesAndGetAfterTimestamp(
                    categories = dtos,
                    timestamp = timestamp,
                    localTimestamp = localTimestamp,
                    token = token
                )
            },
            dataModelToEntityMapper = CategoryDataModel::toEntity,
            entityToCommandDtoMapper = CategoryEntity::toCommandDto,
            queryDtoToEntityMapper = CategoryQueryDto::toEntity
        ).also { result ->
            when (result) {
                is SimpleResult.Success -> println("Categories synchronized successfully.")
                is SimpleResult.Error -> println("Error synchronizing categories: ${result.error}")
            }
        }
    }

    override suspend fun deleteAndUpsertCategories(
        toDelete: List<Category>,
        toUpsert: List<Category>
    ) {
        val toDelete = toDelete.map { it.toDataModel() }
        val toUpsert = toUpsert.map { it.toDataModel() }

        syncHelper.deleteAndUpsertDataSafe(
            tableName = TableName.Category,
            toDelete = toDelete,
            toUpsert = toUpsert,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { token -> remoteSource.getUpdateTime(token = token) },
            localSoftCommand = { entities, timestamp ->
                localSource.saveCategories(categories = entities, timestamp = timestamp)
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndSaveCategories(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            localDeleteCommand = { entities ->
                localSource.deleteCategories(categories = entities)
            },
            remoteSoftCommand = { dtos, timestamp, token ->
                remoteSource.synchronizeCategories(
                    categories = dtos, timestamp = timestamp, token = token
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getCategoriesAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, localTimestamp, token ->
                remoteSource.synchronizeCategoriesAndGetAfterTimestamp(
                    categories = dtos,
                    timestamp = timestamp,
                    localTimestamp = localTimestamp,
                    token = token
                )
            },
            entityDeletedPredicate = { it.deleted },
            dataModelToEntityMapper = CategoryDataModel::toEntity,
            entityToCommandDtoMapper = CategoryEntity::toCommandDto,
            queryDtoToEntityMapper = CategoryQueryDto::toEntity
        ).also { result ->
            when (result) {
                is SimpleResult.Success -> println("Categories deleted and upserted successfully.")
                is SimpleResult.Error -> println("Error deleting and upserting categories: ${result.error}")
            }
        }
    }

    override suspend fun deleteAllCategoriesLocally() {
        localSource.deleteAllCategories()
    }

    override fun getAllCategoriesAsFlow(): Flow<List<Category>> {
        return localSource
            .getAllCategoriesAsFlow()
            .onStart { synchronizeCategories() }
            .map { categories ->
                categories.mapNotNull { it.toDataModel().toDomainModel() }
            }
    }

    override suspend fun getAllCategories(): List<Category> {
        synchronizeCategories()
        return localSource.getAllCategories().mapNotNull { it.toDataModel().toDomainModel() }
    }

    override suspend fun getCategoriesByType(type: Char): List<Category> {
        synchronizeCategories()
        return localSource.getCategoriesByType(type = type).mapNotNull {
            it.toDataModel().toDomainModel()
        }
    }

}