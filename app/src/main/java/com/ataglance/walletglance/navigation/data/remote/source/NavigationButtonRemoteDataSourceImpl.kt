package com.ataglance.walletglance.navigation.data.remote.source

import com.glanci.navigation.shared.dto.NavigationButtonDto
import com.glanci.navigation.shared.service.NavigationButtonService
import com.glanci.request.shared.ResultData
import com.glanci.request.shared.SimpleResult
import com.glanci.request.shared.error.DataError
import com.glanci.request.shared.error.NavigationButtonDataError
import kotlinx.rpc.krpc.ktor.client.KtorRpcClient
import kotlinx.rpc.withService

class NavigationButtonRemoteDataSourceImpl(
    private val service: NavigationButtonService
) : NavigationButtonRemoteDataSource {

    constructor(client: KtorRpcClient) : this(
        service = client.withService<NavigationButtonService>()
    )


    override suspend fun getUpdateTime(token: String): ResultData<Long, DataError> {
        return runCatching {
            service.getUpdateTime(token = token)
        }.getOrDefault(
            defaultValue = ResultData.Error(error = NavigationButtonDataError.NavigationButtonsError)
        )
    }

    override suspend fun synchronizeNavigationButtons(
        buttons: List<NavigationButtonDto>,
        timestamp: Long,
        token: String
    ): SimpleResult<DataError> {
        return runCatching {
            service.saveNavigationButtons(buttons = buttons, timestamp = timestamp, token = token)
        }.getOrDefault(
            defaultValue = SimpleResult.Error(error = NavigationButtonDataError.NavigationButtonsError)
        )
    }

    override suspend fun getNavigationButtonsAfterTimestamp(
        timestamp: Long,
        token: String
    ): ResultData<List<NavigationButtonDto>, DataError> {
        return runCatching {
            service.getNavigationButtonsAfterTimestamp(timestamp = timestamp, token = token)
        }.getOrDefault(
            defaultValue = ResultData.Error(error = NavigationButtonDataError.NavigationButtonsError)
        )
    }

    override suspend fun synchronizeNavigationButtonsAndGetAfterTimestamp(
        buttons: List<NavigationButtonDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): ResultData<List<NavigationButtonDto>, DataError> {
        return runCatching {
            service.saveNavigationButtonsAndGetAfterTimestamp(
                buttons = buttons,
                timestamp = timestamp,
                localTimestamp = localTimestamp,
                token = token
            )
        }.getOrDefault(
            defaultValue = ResultData.Error(error = NavigationButtonDataError.NavigationButtonsError)
        )
    }

}