package com.ataglance.walletglance.budget.data.repository

import com.ataglance.walletglance.budget.data.local.model.BudgetOnWidgetEntity
import com.ataglance.walletglance.budget.data.local.source.BudgetOnWidgetLocalDataSource
import com.ataglance.walletglance.budget.data.mapper.budgetOnWidget.toDataModel
import com.ataglance.walletglance.budget.data.mapper.budgetOnWidget.toDto
import com.ataglance.walletglance.budget.data.mapper.budgetOnWidget.toEntity
import com.ataglance.walletglance.budget.data.model.BudgetOnWidgetDataModel
import com.ataglance.walletglance.budget.data.remote.source.BudgetOnWidgetRemoteDataSource
import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.TableName
import com.glanci.budget.shared.dto.BudgetOnWidgetDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class BudgetOnWidgetRepositoryImpl(
    private val localSource: BudgetOnWidgetLocalDataSource,
    private val remoteSource: BudgetOnWidgetRemoteDataSource,
    private val syncHelper: DataSyncHelper
) : BudgetOnWidgetRepository {

    private suspend fun synchronizeBudgetsOnWidget() {
        syncHelper.synchronizeData(
            tableName = TableName.BudgetOnWidget,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { token -> remoteSource.getUpdateTime(token = token) },
            localDataGetter = { timestamp ->
                localSource.getBudgetsOnWidgetAfterTimestamp(timestamp = timestamp)
            },
            remoteDataGetter = { timestamp, token ->
                remoteSource.getBudgetsOnWidgetAfterTimestamp(timestamp = timestamp, token = token)
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndUpsertBudgetsOnWidget(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            remoteSynchronizer = { data, timestamp, token ->
                remoteSource.synchronizeBudgetsOnWidget(
                    budgets = data, timestamp = timestamp, token = token
                )
            },
            entityDeletedPredicate = { it.deleted },
            entityToCommandDtoMapper = BudgetOnWidgetEntity::toDto,
            queryDtoToEntityMapper = BudgetOnWidgetDto::toEntity
        )
    }

    override suspend fun deleteAndUpsertBudgetsOnWidget(
        toDelete: List<BudgetOnWidgetDataModel>,
        toUpsert: List<BudgetOnWidgetDataModel>
    ) {
        syncHelper.deleteAndUpsertData(
            tableName = TableName.BudgetOnWidget,
            toDelete = toDelete,
            toUpsert = toUpsert,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { token -> remoteSource.getUpdateTime(token = token) },
            localSoftCommand = { entities, timestamp ->
                localSource.upsertBudgetsOnWidget(budgets = entities, timestamp = timestamp)
                entities
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndUpsertBudgetsOnWidget(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            localDeleteCommand = { entities ->
                localSource.deleteBudgetsOnWidget(budgets = entities)
            },
            remoteSoftCommand = { dtos, timestamp, token ->
                remoteSource.synchronizeBudgetsOnWidget(
                    budgets = dtos, timestamp = timestamp, token = token
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getBudgetsOnWidgetAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, localTimestamp, token ->
                remoteSource.synchronizeBudgetsOnWidgetAndGetAfterTimestamp(
                    budgets = dtos,
                    timestamp = timestamp,
                    localTimestamp = localTimestamp,
                    token = token
                )
            },
            entityDeletedPredicate = { it.deleted },
            dataModelToEntityMapper = BudgetOnWidgetDataModel::toEntity,
            entityToCommandDtoMapper = BudgetOnWidgetEntity::toDto,
            queryDtoToEntityMapper = BudgetOnWidgetDto::toEntity
        )
    }

    override fun getAllBudgetsOnWidgetAsFlow(): Flow<List<BudgetOnWidgetDataModel>> {
        return localSource
            .getAllBudgetsOnWidgetAsFlow()
            .onStart { synchronizeBudgetsOnWidget() }
            .map { budgets ->
                budgets.map { it.toDataModel() }
            }
    }

    override suspend fun getAllBudgetsOnWidget(): List<BudgetOnWidgetDataModel> {
        synchronizeBudgetsOnWidget()
        return localSource.getAllBudgetsOnWidget().map { it.toDataModel() }
    }

}