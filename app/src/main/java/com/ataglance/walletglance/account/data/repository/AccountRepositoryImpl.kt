package com.ataglance.walletglance.account.data.repository

import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.account.data.local.source.AccountLocalDataSource
import com.ataglance.walletglance.account.data.mapper.toCommandDto
import com.ataglance.walletglance.account.data.mapper.toDataModel
import com.ataglance.walletglance.account.data.mapper.toEntity
import com.ataglance.walletglance.account.data.model.AccountDataModel
import com.ataglance.walletglance.account.data.remote.source.AccountRemoteDataSource
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.repository.AccountRepository
import com.ataglance.walletglance.account.mapper.toDataModel
import com.ataglance.walletglance.account.mapper.toDomainModel
import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.TableName
import com.glanci.account.shared.dto.AccountQueryDto
import com.glanci.request.shared.SimpleResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class AccountRepositoryImpl(
    private val localSource: AccountLocalDataSource,
    private val remoteSource: AccountRemoteDataSource,
    private val syncHelper: DataSyncHelper
) : AccountRepository {

    private suspend fun synchronizeAccounts() {
        syncHelper.synchronizeDataSafe(
            tableName = TableName.Account,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { token -> remoteSource.getUpdateTime(token = token) },
            localDataGetter = { timestamp ->
                localSource.getAccountsAfterTimestamp(timestamp = timestamp)
            },
            remoteDataGetter = { timestamp, token ->
                remoteSource.getAccountsAfterTimestamp(timestamp = timestamp, token = token)
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndSaveAccounts(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            remoteSynchronizer = { data, timestamp, token ->
                remoteSource.synchronizeAccounts(
                    accounts = data, timestamp = timestamp, token = token
                )
            },
            entityDeletedPredicate = { it.deleted },
            entityToCommandDtoMapper = AccountEntity::toCommandDto,
            queryDtoToEntityMapper = AccountQueryDto::toEntity
        ).also { result ->
            when (result) {
                is SimpleResult.Success -> println("Accounts synchronized successfully.")
                is SimpleResult.Error -> println("Error synchronizing accounts: ${result.error}")
            }
        }
    }

    override suspend fun upsertAccounts(accounts: List<Account>) {
        val accounts = accounts.map { it.toDataModel() }

        syncHelper.upsertDataSafe(
            tableName = TableName.Account,
            data = accounts,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { token -> remoteSource.getUpdateTime(token = token) },
            localSoftCommand = { entities, timestamp ->
                localSource.saveAccounts(accounts = entities, timestamp = timestamp)
            },
            remoteSoftCommand = { dtos, timestamp, token ->
                remoteSource.synchronizeAccounts(
                    accounts = dtos, timestamp = timestamp, token = token
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getAccountsAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, localTimestamp, token ->
                remoteSource.synchronizeAccountsAndGetAfterTimestamp(
                    accounts = dtos,
                    timestamp = timestamp,
                    localTimestamp = localTimestamp,
                    token = token
                )
            },
            dataModelToEntityMapper = AccountDataModel::toEntity,
            entityToCommandDtoMapper = AccountEntity::toCommandDto,
            queryDtoToEntityMapper = AccountQueryDto::toEntity
        ).also { result ->
            when (result) {
                is SimpleResult.Success -> println("Accounts upserted successfully.")
                is SimpleResult.Error -> println("Error upserting accounts: ${result.error}")
            }
        }
    }

    override suspend fun deleteAndUpsertAccounts(
        toDelete: List<Account>,
        toUpsert: List<Account>
    ) {
        val toDelete = toDelete.map { it.toDataModel() }
        val toUpsert = toUpsert.map { it.toDataModel() }

        syncHelper.deleteAndUpsertDataSafe(
            tableName = TableName.Account,
            toDelete = toDelete,
            toUpsert = toUpsert,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { token -> remoteSource.getUpdateTime(token = token) },
            localSoftCommand = { entities, timestamp ->
                localSource.saveAccounts(accounts = entities, timestamp = timestamp)
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndSaveAccounts(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            localDeleteCommand = { entities ->
                localSource.deleteAccounts(accounts = entities)
            },
            remoteSoftCommand = { dtos, timestamp, token ->
                remoteSource.synchronizeAccounts(
                    accounts = dtos, timestamp = timestamp, token = token
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getAccountsAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, localTimestamp, token ->
                remoteSource.synchronizeAccountsAndGetAfterTimestamp(
                    accounts = dtos,
                    timestamp = timestamp,
                    localTimestamp = localTimestamp,
                    token = token
                )
            },
            entityDeletedPredicate = { it.deleted },
            dataModelToEntityMapper = AccountDataModel::toEntity,
            entityToCommandDtoMapper = AccountEntity::toCommandDto,
            queryDtoToEntityMapper = AccountQueryDto::toEntity
        ).also { result ->
            when (result) {
                is SimpleResult.Success -> println("Accounts deleted and upserted successfully.")
                is SimpleResult.Error -> println("Error deleting and upserting accounts: ${result.error}")
            }
        }
    }

    override suspend fun deleteAllAccountsLocally() {
        localSource.deleteAllAccounts()
    }

    override suspend fun getAccount(id: Int): Account? {
        synchronizeAccounts()
        return localSource.getAccount(id = id)?.toDataModel()?.toDomainModel()
    }

    override fun getAllAccountsAsFlow(): Flow<List<Account>> {
        return localSource
            .getAllAccountsAsFlow()
            .onStart { synchronizeAccounts() }
            .map { accounts ->
                accounts.map { it.toDataModel().toDomainModel() }
            }
    }

    override suspend fun getAllAccounts(): List<Account> {
        synchronizeAccounts()
        return localSource.getAllAccounts().map { it.toDataModel().toDomainModel() }
    }

}