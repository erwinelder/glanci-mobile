package com.ataglance.walletglance.core.domain.repository

interface LocalUpdateTimeRepository {

    suspend fun deleteAllUpdateTimes()

}