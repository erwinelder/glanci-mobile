package com.ataglance.walletglance.budget.data.repository

import com.ataglance.walletglance.budget.data.local.model.BudgetEntityWithAssociations
import com.ataglance.walletglance.budget.data.local.source.BudgetLocalDataSource
import com.ataglance.walletglance.budget.data.mapper.budget.toDataModel
import com.ataglance.walletglance.budget.data.mapper.budget.toDataModelWithAssociations
import com.ataglance.walletglance.budget.data.mapper.budget.toDtoWithAssociations
import com.ataglance.walletglance.budget.data.mapper.budget.toEntityWithAssociations
import com.ataglance.walletglance.budget.data.model.BudgetWithAssociationsDataModel
import com.ataglance.walletglance.budget.data.remote.source.BudgetRemoteDataSource
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.BudgetWithIds
import com.ataglance.walletglance.budget.mapper.budget.toBudgetWithIds
import com.ataglance.walletglance.budget.mapper.budget.toDataModelWithAssociations
import com.ataglance.walletglance.budget.mapper.budget.toDomainModel
import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.TableName
import com.glanci.budget.shared.dto.BudgetWithAssociationsDto
import com.glanci.request.shared.SimpleResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class BudgetRepositoryImpl(
    private val localSource: BudgetLocalDataSource,
    private val remoteSource: BudgetRemoteDataSource,
    private val syncHelper: DataSyncHelper
) : BudgetRepository {

    private suspend fun synchronizeBudgetsWithAssociations() {
        syncHelper.synchronizeDataSafe(
            tableName = TableName.Account,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { token -> remoteSource.getUpdateTime(token = token) },
            localDataGetter = { timestamp ->
                localSource.getBudgetsWithAssociationsAfterTimestamp(timestamp = timestamp)
            },
            remoteDataGetter = { timestamp, token ->
                remoteSource.getBudgetsWithAssociationsAfterTimestamp(
                    timestamp = timestamp, token = token
                )
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndUpsertBudgetsWithAssociations(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            remoteSynchronizer = { data, timestamp, token ->
                remoteSource.synchronizeBudgetsWithAssociations(
                    budgets = data, timestamp = timestamp, token = token
                )
            },
            entityDeletedPredicate = { it.deleted },
            entityToCommandDtoMapper = BudgetEntityWithAssociations::toDtoWithAssociations,
            queryDtoToEntityMapper = BudgetWithAssociationsDto::toEntityWithAssociations,
        ).also { result ->
            when (result) {
                is SimpleResult.Success -> println("Budgets with associations synchronized successfully.")
                is SimpleResult.Error -> println("Error synchronizing budgets with associations: ${result.error}")
            }
        }
    }

    override suspend fun deleteAndUpsertBudgetsWithAccountIds(
        toDelete: List<Budget>,
        toUpsert: List<BudgetWithIds>
    ) {
        val toDelete = toDelete.map { it.toDataModelWithAssociations() }
        val toUpsert = toUpsert.map { it.toDataModelWithAssociations() }

        syncHelper.deleteAndUpsertDataSafe(
            tableName = TableName.Budget,
            toDelete = toDelete,
            toUpsert = toUpsert,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { token -> remoteSource.getUpdateTime(token = token) },
            localSoftCommand = { entities, timestamp ->
                localSource.upsertBudgetsWithAssociations(
                    budgetsWithAssociations = entities, timestamp = timestamp
                )
                entities
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndUpsertBudgetsWithAssociations(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            localDeleteCommand = { entities ->
                localSource.deleteBudgetsWithAssociations(budgetsWithAssociations = entities)
            },
            remoteSoftCommand = { dtos, timestamp, token ->
                remoteSource.synchronizeBudgetsWithAssociations(
                    budgets = dtos, timestamp = timestamp, token = token
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getBudgetsWithAssociationsAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, localTimestamp, token ->
                remoteSource.synchronizeBudgetsWithAssociationsAndGetAfterTimestamp(
                    budgets = dtos,
                    timestamp = timestamp,
                    localTimestamp = localTimestamp,
                    token = token
                )
            },
            entityDeletedPredicate = { it.deleted },
            dataModelToEntityMapper = BudgetWithAssociationsDataModel::toEntityWithAssociations,
            entityToCommandDtoMapper = BudgetEntityWithAssociations::toDtoWithAssociations,
            queryDtoToEntityMapper = BudgetWithAssociationsDto::toEntityWithAssociations
        ).also { result ->
            when (result) {
                is SimpleResult.Success -> println("Budgets with associations deleted and upserted successfully.")
                is SimpleResult.Error -> println("Error deleting and upserting budgets with associations: ${result.error}")
            }
        }
    }

    override suspend fun getBudgetWithAccountIds(budgetId: Int): BudgetWithIds? {
        synchronizeBudgetsWithAssociations()
        return localSource.getBudgetWithAssociations(budgetId = budgetId)
            ?.toDataModelWithAssociations()
            ?.toBudgetWithIds()
    }

    override fun getBudgetWithAccountIdsByIdsAsFlow(
        budgetIds: List<Int>
    ): Flow<List<BudgetWithIds>> {
        return localSource.getBudgetsWithAssociationsByIdsAsFlow(budgetIds = budgetIds)
            .onStart { synchronizeBudgetsWithAssociations() }
            .map { budgetsWithAssociations ->
                budgetsWithAssociations.mapNotNull {
                    it.toDataModelWithAssociations().toBudgetWithIds()
                }
            }
    }

    override suspend fun getAllBudgets(): List<Budget> {
        synchronizeBudgetsWithAssociations()
        return localSource.getAllBudgets().mapNotNull { it.toDataModel().toDomainModel() }
    }

    override fun getAllBudgetsWithAccountIdsAsFlow(): Flow<List<BudgetWithIds>> {
        return localSource.getAllBudgetsWithAssociationsAsFlow()
            .onStart { synchronizeBudgetsWithAssociations() }
            .map { budgetsWithAssociations ->
                budgetsWithAssociations.mapNotNull {
                    it.toDataModelWithAssociations().toBudgetWithIds()
                }
            }
    }

    override suspend fun getAllBudgetsWithAccountIds(): List<BudgetWithIds> {
        synchronizeBudgetsWithAssociations()
        return localSource.getAllBudgetsWithAssociations().mapNotNull {
            it.toDataModelWithAssociations().toBudgetWithIds()
        }
    }

}