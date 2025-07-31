package com.ataglance.walletglance.core.data.repository

import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.core.domain.repository.LocalUpdateTimeRepository

class LocalUpdateTimeRepositoryImpl(
    private val localUpdateTimeDao: LocalUpdateTimeDao
) : LocalUpdateTimeRepository {

    override suspend fun deleteAllUpdateTimes() {
        localUpdateTimeDao.deleteAllUpdateTimes()
    }

}

fun getLocalUpdateTimeRepository(appDatabase: AppDatabase): LocalUpdateTimeRepository {
    return LocalUpdateTimeRepositoryImpl(
        localUpdateTimeDao = appDatabase.localUpdateTimeDao
    )
}