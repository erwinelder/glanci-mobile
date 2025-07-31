package com.ataglance.walletglance.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ataglance.walletglance.core.data.local.model.LocalUpdateTime

@Dao
interface LocalUpdateTimeDao {

    @Upsert
    suspend fun saveUpdateTime(entity: LocalUpdateTime)

    suspend fun saveUpdateTime(tableName: String, timestamp: Long) {
        saveUpdateTime(
            entity = LocalUpdateTime(tableName = tableName, timestamp = timestamp)
        )
    }

    @Delete
    suspend fun deleteUpdateTime(entity: LocalUpdateTime)

    suspend fun deleteUpdateTime(tableName: String) {
        deleteUpdateTime(
            entity = LocalUpdateTime(tableName = tableName, timestamp = 0)
        )
    }

    @Query("DELETE FROM local_update_time")
    suspend fun deleteAllUpdateTimes()

    @Query("SELECT timestamp FROM local_update_time WHERE tableName = :tableName")
    suspend fun getUpdateTime(tableName: String): Long?

}