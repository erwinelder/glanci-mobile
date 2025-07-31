package com.ataglance.walletglance.personalization.data.remote.source

import com.glanci.personalization.shared.dto.WidgetDto
import com.glanci.personalization.shared.service.WidgetService
import com.glanci.request.shared.ResultData
import com.glanci.request.shared.SimpleResult
import com.glanci.request.shared.error.DataError
import com.glanci.request.shared.error.WidgetDataError
import kotlinx.rpc.krpc.ktor.client.KtorRpcClient
import kotlinx.rpc.withService

class WidgetRemoteDataSourceImpl(
    private val service: WidgetService
) : WidgetRemoteDataSource {

    constructor(client: KtorRpcClient) : this(service = client.withService<WidgetService>())


    override suspend fun getUpdateTime(token: String): ResultData<Long, DataError> {
        return runCatching {
            service.getUpdateTime(token = token)
        }.getOrDefault(
            defaultValue = ResultData.Error(error = WidgetDataError.WidgetError)
        )
    }

    override suspend fun synchronizeWidgets(
        widgets: List<WidgetDto>,
        timestamp: Long,
        token: String
    ): SimpleResult<DataError> {
        return runCatching {
            service.saveWidgets(widgets = widgets, timestamp = timestamp, token = token)
        }.getOrDefault(
            defaultValue = SimpleResult.Error(error = WidgetDataError.WidgetError)
        )
    }

    override suspend fun getWidgetsAfterTimestamp(timestamp: Long, token: String): ResultData<List<WidgetDto>, DataError> {
        return runCatching {
            service.getWidgetsAfterTimestamp(timestamp = timestamp, token = token)
        }.getOrDefault(
            defaultValue = ResultData.Error(error = WidgetDataError.WidgetError)
        )
    }

    override suspend fun synchronizeWidgetsAndGetAfterTimestamp(
        widgets: List<WidgetDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): ResultData<List<WidgetDto>, DataError> {
        return runCatching {
            service.saveWidgetsAndGetAfterTimestamp(
                widgets = widgets,
                timestamp = timestamp,
                localTimestamp = localTimestamp,
                token = token
            )
        }.getOrDefault(
            defaultValue = ResultData.Error(error = WidgetDataError.WidgetError)
        )
    }

}