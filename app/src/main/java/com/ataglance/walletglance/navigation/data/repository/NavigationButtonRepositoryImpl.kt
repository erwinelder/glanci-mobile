package com.ataglance.walletglance.navigation.data.repository

import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.navigation.data.local.model.NavigationButtonEntity
import com.ataglance.walletglance.navigation.data.local.source.NavigationButtonLocalDataSource
import com.ataglance.walletglance.navigation.data.mapper.toDataModel
import com.ataglance.walletglance.navigation.data.mapper.toDto
import com.ataglance.walletglance.navigation.data.mapper.toEntity
import com.ataglance.walletglance.navigation.data.model.NavigationButtonDataModel
import com.ataglance.walletglance.navigation.data.remote.source.NavigationButtonRemoteDataSource
import com.ataglance.walletglance.navigation.domain.model.AppScreenEnum
import com.ataglance.walletglance.navigation.domain.repository.NavigationButtonRepository
import com.ataglance.walletglance.navigation.mapper.toDataModels
import com.ataglance.walletglance.navigation.mapper.toDomainModelsSorted
import com.glanci.navigation.shared.dto.NavigationButtonDto
import com.glanci.request.shared.SimpleResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class NavigationButtonRepositoryImpl(
    private val localSource: NavigationButtonLocalDataSource,
    private val remoteSource: NavigationButtonRemoteDataSource,
    private val syncHelper: DataSyncHelper
) : NavigationButtonRepository {

    private suspend fun synchronizeNavigationButtons() {
        syncHelper.synchronizeDataSafe(
            tableName = TableName.NavigationButton,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { token -> remoteSource.getUpdateTime(token = token) },
            localDataGetter = { timestamp ->
                localSource.getNavigationButtonsAfterTimestamp(timestamp = timestamp)
            },
            remoteDataGetter = { timestamp, token ->
                remoteSource.getNavigationButtonsAfterTimestamp(timestamp = timestamp, token = token)
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndUpsertNavigationButtons(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            remoteSynchronizer = { data, timestamp, token ->
                remoteSource.synchronizeNavigationButtons(
                    buttons = data, timestamp = timestamp, token = token
                )
            },
            entityDeletedPredicate = { it.deleted },
            entityToCommandDtoMapper = NavigationButtonEntity::toDto,
            queryDtoToEntityMapper = NavigationButtonDto::toEntity
        ).also { result ->
            when (result) {
                is SimpleResult.Success -> println("Navigation buttons synchronized successfully.")
                is SimpleResult.Error -> println("Error synchronizing navigation buttons: ${result.error}")
            }
        }
    }

    override suspend fun upsertNavigationButtons(screens: List<AppScreenEnum>) {
        val buttons = screens.toDataModels()

        syncHelper.upsertDataSafe(
            tableName = TableName.NavigationButton,
            data = buttons,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { token -> remoteSource.getUpdateTime(token = token) },
            localSoftCommand = { entities, timestamp ->
                localSource.upsertNavigationButtons(buttons = entities, timestamp = timestamp)
                entities
            },
            remoteSoftCommand = { dtos, timestamp, token ->
                remoteSource.synchronizeNavigationButtons(
                    buttons = dtos, timestamp = timestamp, token = token
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getNavigationButtonsAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, localTimestamp, token ->
                remoteSource.synchronizeNavigationButtonsAndGetAfterTimestamp(
                    buttons = dtos,
                    timestamp = timestamp,
                    localTimestamp = localTimestamp,
                    token = token
                )
            },
            dataModelToEntityMapper = NavigationButtonDataModel::toEntity,
            entityToCommandDtoMapper = NavigationButtonEntity::toDto,
            queryDtoToEntityMapper = NavigationButtonDto::toEntity
        ).also { result ->
            when (result) {
                is SimpleResult.Success -> println("Navigation buttons upserted successfully.")
                is SimpleResult.Error -> println("Error upserting navigation buttons: ${result.error}")
            }
        }
    }

    override suspend fun deleteAllNavigationButtonsLocally() {
        localSource.deleteAllNavigationButtons()
    }

    override fun getAllNavigationButtonsAsFlow(): Flow<List<AppScreenEnum>> {
        return localSource
            .getAllNavigationButtonsAsFlow()
            .onStart { synchronizeNavigationButtons() }
            .map { buttons ->
                buttons.map { it.toDataModel() }.toDomainModelsSorted()
            }
    }

}