package com.ataglance.walletglance.budget.data.repository

import com.ataglance.walletglance.budget.data.local.model.BudgetEntityWithAssociations
import com.ataglance.walletglance.budget.data.local.source.BudgetLocalDataSource
import com.ataglance.walletglance.budget.data.mapper.budget.toDataModel
import com.ataglance.walletglance.budget.data.mapper.budget.toDataModelWithAssociations
import com.ataglance.walletglance.budget.data.mapper.budget.toDtoWithAssociations
import com.ataglance.walletglance.budget.data.mapper.budget.toEntityWithAssociations
import com.ataglance.walletglance.budget.data.mapper.budget.withAssociations
import com.ataglance.walletglance.budget.data.model.BudgetDataModel
import com.ataglance.walletglance.budget.data.model.BudgetWithAssociationsDataModel
import com.glanci.budget.shared.dto.BudgetWithAssociationsDto
import com.ataglance.walletglance.budget.data.remote.source.BudgetRemoteDataSource
import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.TableName

class BudgetRepositoryImpl(
    private val localSource: BudgetLocalDataSource,
    private val remoteSource: BudgetRemoteDataSource,
    private val syncHelper: DataSyncHelper
) : BudgetRepository {

    private suspend fun synchronizeBudgetsWithAssociations() {
        syncHelper.synchronizeDataToken(
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
        )
    }

    override suspend fun deleteAndUpsertBudgetsWithAssociations(
        toDelete: List<BudgetDataModel>,
        toUpsert: List<BudgetWithAssociationsDataModel>
    ) {
        syncHelper.deleteAndUpsertDataToken(
            tableName = TableName.Budget,
            toDelete = toDelete.map { it.withAssociations() },
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
            dataModelToCommandDtoMapper = BudgetWithAssociationsDataModel::toDtoWithAssociations,
            entityToCommandDtoMapper = BudgetEntityWithAssociations::toDtoWithAssociations,
            queryDtoToEntityMapper = BudgetWithAssociationsDto::toEntityWithAssociations
        )
    }

    override suspend fun getAllBudgets(): List<BudgetDataModel> {
        synchronizeBudgetsWithAssociations()
        return localSource.getAllBudgets().map { it.toDataModel() }
    }

    override suspend fun getBudgetWithAssociations(
        budgetId: Int
    ): BudgetWithAssociationsDataModel? {
        synchronizeBudgetsWithAssociations()
        return localSource.getBudgetWithAssociations(budgetId = budgetId)
            ?.toDataModelWithAssociations()
    }

    override suspend fun getAllBudgetsWithAssociations(): List<BudgetWithAssociationsDataModel> {
        synchronizeBudgetsWithAssociations()
        return localSource.getAllBudgetsWithAssociations().map { it.toDataModelWithAssociations() }
    }

}