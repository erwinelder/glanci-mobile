package com.ataglance.walletglance.core.data.model

import com.ataglance.walletglance.auth.domain.model.user.UserContext
import com.ataglance.walletglance.core.utils.getCurrentTimestamp

class DataSyncHelper(
    private val tablesSyncContext: TablesSyncContext = TablesSyncContext,
    private val userContext: UserContext
) {

    fun getUserTokenForSync(): String? {
        return userContext.takeIf { it.isEligibleForDataSync() }?.getAuthToken()
    }

    fun getUserTokenForSync(tableName: TableName): String? {
        return if (tablesSyncContext.tableNotUpdated(tableName = tableName)) {
            getUserTokenForSync()
        } else null
    }

    fun setTableSynced(tableName: TableName) {
        tablesSyncContext.setTableUpdated(tableName = tableName)
    }


    suspend fun <E, QD, CD> synchronizeData(
        tableName: TableName,
        localTimestampGetter: suspend () -> Long?,
        remoteTimestampGetter: suspend (token: String) -> Long?,
        localDataGetter: suspend (timestamp: Long) -> List<E>,
        remoteDataGetter: suspend (timestamp: Long, token: String) -> List<QD>?,
        localHardCommand: suspend (toDelete: List<E>, toUpsert: List<E>, timestamp: Long) -> Unit,
        remoteSynchronizer: suspend (List<CD>, timestamp: Long, token: String) -> Boolean,
        entityDeletedPredicate: (E) -> Boolean,
        entityToCommandDtoMapper: (E) -> CD,
        queryDtoToEntityMapper: (QD) -> E
    ) {
        val userToken = getUserTokenForSync(tableName = tableName) ?: return
        val localTimestamp = localTimestampGetter() ?: 0
        val remoteTimestamp = remoteTimestampGetter(userToken) ?: return

        when {
            // Synchronize from local to remote
            localTimestamp > remoteTimestamp -> {
                val dataToSync = localDataGetter(remoteTimestamp).map(entityToCommandDtoMapper)
                val result = remoteSynchronizer(dataToSync, localTimestamp, userToken)
                if (result) setTableSynced(tableName = tableName)
            }
            // Synchronize from remote to local
            localTimestamp < remoteTimestamp -> {
                val (entitiesToDelete, entitiesToUpsert) = remoteDataGetter(localTimestamp, userToken)
                    ?.map(queryDtoToEntityMapper)
                    ?.partition(entityDeletedPredicate)
                    ?: return
                localHardCommand(entitiesToDelete, entitiesToUpsert, remoteTimestamp)
            }
            // Data is up to date, no synchronization needed
            else -> {
                setTableSynced(tableName = tableName)
            }
        }
    }

    suspend fun <DM, E, QD, CD> upsertData(
        tableName: TableName,
        data: List<DM>,
        localTimestampGetter: suspend () -> Long?,
        remoteTimestampGetter: suspend (token: String) -> Long?,
        localSoftCommand: suspend (entities: List<E>, timestamp: Long) -> List<E>,
        remoteSoftCommand: suspend (dtos: List<CD>, timestamp: Long, token: String) -> Boolean,
        localDataAfterTimestampGetter: suspend (timestamp: Long) -> List<E>,
        remoteSoftCommandAndDataAfterTimestampGetter: suspend (
            dtos: List<CD>,
            timestamp: Long,
            localTimestamp: Long,
            token: String
        ) -> List<QD>?,
        dataModelToEntityMapper: (DM, timestamp: Long, deleted: Boolean) -> E,
        entityToCommandDtoMapper: (E) -> CD,
        queryDtoToEntityMapper: (QD) -> E
    ) {
        val timestamp = getCurrentTimestamp()
        val userToken = getUserTokenForSync()

        // Data cannot be synchronized with remote, save locally
        if (userToken == null) {
            val entities = data.map { dataModelToEntityMapper(it, timestamp, false) }
            localSoftCommand(entities, timestamp)
            return
        }

        val localTimestamp = localTimestampGetter() ?: 0
        val remoteTimestamp = remoteTimestampGetter(userToken) ?: return

        when {
            // Local data is newer, save locally and synchronize from local to remote
            localTimestamp > remoteTimestamp -> {
                val entities = data.map { dataModelToEntityMapper(it, timestamp, false) }
                localSoftCommand(entities, timestamp)
                val dtos = localDataAfterTimestampGetter(remoteTimestamp).map(entityToCommandDtoMapper)
                val result = remoteSoftCommand(dtos, timestamp, userToken)
                if (result) setTableSynced(tableName = tableName)
            }
            // Remote data is newer, save remotely and synchronize from remote to local
            localTimestamp < remoteTimestamp -> {
                val entities = data.map { dataModelToEntityMapper(it, timestamp, false) }
                val dtos = localSoftCommand(entities, timestamp).map(entityToCommandDtoMapper)
                val remoteEntities = remoteSoftCommandAndDataAfterTimestampGetter(
                    dtos, timestamp, localTimestamp, userToken
                )?.map(queryDtoToEntityMapper)

                if (remoteEntities != null) {
                    localSoftCommand(remoteEntities, timestamp)
                    setTableSynced(tableName = tableName)
                }
            }
            // Data is up to date, save locally and remotely
            else -> {
                val entities = data.map { dataModelToEntityMapper(it, timestamp, false) }
                val dtos = localSoftCommand(entities, timestamp).map(entityToCommandDtoMapper)
                val result = remoteSoftCommand(dtos, timestamp, userToken)
                if (result) setTableSynced(tableName = tableName)
            }
        }
    }

    suspend fun <DM, E, QD, CD> deleteData(
        tableName: TableName,
        data: List<DM>,
        localTimestampGetter: suspend () -> Long?,
        remoteTimestampGetter: suspend (token: String) -> Long?,
        localSoftCommand: suspend (entities: List<E>, timestamp: Long) -> Unit,
        localHardCommand: suspend (toDelete: List<E>, toUpsert: List<E>, timestamp: Long) -> Unit,
        localDeleteCommand: suspend (entities: List<E>, timestamp: Long?) -> Unit,
        remoteSoftCommand: suspend (dtos: List<CD>, timestamp: Long, token: String) -> Boolean,
        localDataAfterTimestampGetter: suspend (timestamp: Long) -> List<E>,
        remoteSoftCommandAndDataAfterTimestampGetter: suspend (
            dtos: List<CD>,
            timestamp: Long,
            localTimestamp: Long,
            token: String
        ) -> List<QD>?,
        entityDeletedPredicate: (E) -> Boolean,
        dataModelToEntityMapper: (DM, timestamp: Long, deleted: Boolean) -> E,
        dataModelToCommandDtoMapper: (DM, timestamp: Long, deleted: Boolean) -> CD,
        entityToCommandDtoMapper: (E) -> CD,
        queryDtoToEntityMapper: (QD) -> E
    ) {
        val timestamp = getCurrentTimestamp()
        val userToken = getUserTokenForSync()

        // Data cannot be synchronized with remote, hard save locally
        if (userToken == null) {
            val entities = data.map { dataModelToEntityMapper(it, timestamp, true) }
            localDeleteCommand(entities, timestamp)
            return
        }

        val localTimestamp = localTimestampGetter() ?: 0
        val remoteTimestamp = remoteTimestampGetter(userToken) ?: return

        when {
            // Local data is newer, save locally and synchronize from local to remote
            localTimestamp > remoteTimestamp -> {
                val entitiesToDelete = data.map { dataModelToEntityMapper(it, timestamp, true) }
                localSoftCommand(entitiesToDelete, timestamp)

                val dtos = localDataAfterTimestampGetter(remoteTimestamp).map(entityToCommandDtoMapper)
                val result = remoteSoftCommand(dtos, timestamp, userToken)

                if (result) {
                    localDeleteCommand(entitiesToDelete, null)
                    setTableSynced(tableName = tableName)
                }
            }
            // Remote data is newer, save remotely and synchronize from remote to local
            localTimestamp < remoteTimestamp -> {
                val dtos = data.map { dataModelToCommandDtoMapper(it, timestamp, true) }
                val entities = remoteSoftCommandAndDataAfterTimestampGetter(
                    dtos, timestamp, localTimestamp, userToken
                )?.map(queryDtoToEntityMapper)

                if (entities != null) {
                    val (entitiesToDelete, entitiesToUpsert) = entities.partition(entityDeletedPredicate)
                    localHardCommand(entitiesToDelete, entitiesToUpsert, timestamp)
                    setTableSynced(tableName = tableName)
                } else {
                    val entities = data.map { dataModelToEntityMapper(it, timestamp, true) }
                    localSoftCommand(entities, timestamp)
                }
            }
            // Data is up to date, save locally and remotely
            else -> {
                val dtos = data.map { dataModelToCommandDtoMapper(it, timestamp, true) }
                val result = remoteSoftCommand(dtos, timestamp, userToken)

                val entitiesToDelete = data.map { dataModelToEntityMapper(it, timestamp, true) }
                if (result) {
                    localDeleteCommand(entitiesToDelete, timestamp)
                    setTableSynced(tableName = tableName)
                } else {
                    localSoftCommand(entitiesToDelete, timestamp)
                }
            }
        }
    }

    suspend fun <DM, E, QD, CD> deleteAndUpsertData(
        tableName: TableName,
        toDelete: List<DM>,
        toUpsert: List<DM>,
        localTimestampGetter: suspend () -> Long?,
        remoteTimestampGetter: suspend (token: String) -> Long?,
        localSoftCommand: suspend (entities: List<E>, timestamp: Long) -> List<E>,
        localHardCommand: suspend (toDelete: List<E>, toUpsert: List<E>, timestamp: Long) -> Unit,
        localDeleteCommand: suspend (entities: List<E>) -> Unit,
        remoteSoftCommand: suspend (dtos: List<CD>, timestamp: Long, token: String) -> Boolean,
        localDataAfterTimestampGetter: suspend (timestamp: Long) -> List<E>,
        remoteSoftCommandAndDataAfterTimestampGetter: suspend (
            dtos: List<CD>,
            timestamp: Long,
            localTimestamp: Long,
            token: String
        ) -> List<QD>?,
        entityDeletedPredicate: (E) -> Boolean,
        dataModelToEntityMapper: (DM, timestamp: Long, deleted: Boolean) -> E,
        entityToCommandDtoMapper: (E) -> CD,
        queryDtoToEntityMapper: (QD) -> E
    ) {
        val timestamp = getCurrentTimestamp()
        val userToken = getUserTokenForSync()

        // Data cannot be synchronized with remote, hard save locally
        if (userToken == null) {
            val entitiesToDelete = toDelete.map { dataModelToEntityMapper(it, timestamp, true) }
            val entitiesToUpsert = toUpsert.map { dataModelToEntityMapper(it, timestamp, false) }
            localHardCommand(entitiesToDelete, entitiesToUpsert, timestamp)
            return
        }

        val localTimestamp = localTimestampGetter() ?: 0
        val remoteTimestamp = remoteTimestampGetter(userToken) ?: return

        when {
            // Local data is newer, save locally and synchronize from local to remote
            localTimestamp > remoteTimestamp -> {
                val entitiesToDelete = toDelete.map { dataModelToEntityMapper(it, timestamp, true) }
                val entitiesToUpsert = toUpsert.map { dataModelToEntityMapper(it, timestamp, false) }
                localSoftCommand(entitiesToDelete + entitiesToUpsert, timestamp)

                val entities = localDataAfterTimestampGetter(remoteTimestamp)
                val dtos = entities.map(entityToCommandDtoMapper)
                val result = remoteSoftCommand(dtos, timestamp, userToken)

                if (result) {
                    localDeleteCommand(entities.filter(entityDeletedPredicate))
                    setTableSynced(tableName = tableName)
                }
            }
            // Remote data is newer, save remotely and synchronize from remote to local
            localTimestamp < remoteTimestamp -> {
                val entities = toDelete.map { dataModelToEntityMapper(it, timestamp, true) } +
                        toUpsert.map { dataModelToEntityMapper(it, timestamp, false) }
                val dtos = localSoftCommand(entities, timestamp).map(entityToCommandDtoMapper)

                val remoteEntities = remoteSoftCommandAndDataAfterTimestampGetter(
                    dtos, timestamp, localTimestamp, userToken
                )?.map(queryDtoToEntityMapper)

                if (remoteEntities != null) {
                    val (entitiesToDelete, entitiesToUpsert) = remoteEntities
                        .partition(entityDeletedPredicate)
                    localHardCommand(entitiesToDelete, entitiesToUpsert, timestamp)
                    setTableSynced(tableName = tableName)
                }
            }
            // Data is up to date, save remotely and locally
            else -> {
                val entities = toDelete.map { dataModelToEntityMapper(it, timestamp, true) } +
                        toUpsert.map { dataModelToEntityMapper(it, timestamp, false) }
                val dtos = localSoftCommand(entities, timestamp).map(entityToCommandDtoMapper)
                val result = remoteSoftCommand(dtos, timestamp, userToken)

                if (result) {
                    val entitiesToDelete = toDelete.map { dataModelToEntityMapper(it, timestamp, true) }
                    localDeleteCommand(entitiesToDelete)
                    setTableSynced(tableName = tableName)
                }
            }
        }
    }

}