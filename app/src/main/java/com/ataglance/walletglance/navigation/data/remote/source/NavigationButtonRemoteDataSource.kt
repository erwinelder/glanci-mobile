package com.ataglance.walletglance.navigation.data.remote.source

import com.glanci.navigation.shared.dto.NavigationButtonDto
import com.glanci.request.shared.ResultData
import com.glanci.request.shared.SimpleResult
import com.glanci.request.shared.error.DataError

interface NavigationButtonRemoteDataSource {

    suspend fun getUpdateTime(token: String): ResultData<Long, DataError>

    suspend fun synchronizeNavigationButtons(
        buttons: List<NavigationButtonDto>,
        timestamp: Long,
        token: String
    ): SimpleResult<DataError>

    suspend fun getNavigationButtonsAfterTimestamp(
        timestamp: Long,
        token: String
    ): ResultData<List<NavigationButtonDto>, DataError>

    suspend fun synchronizeNavigationButtonsAndGetAfterTimestamp(
        buttons: List<NavigationButtonDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): ResultData<List<NavigationButtonDto>, DataError>

}