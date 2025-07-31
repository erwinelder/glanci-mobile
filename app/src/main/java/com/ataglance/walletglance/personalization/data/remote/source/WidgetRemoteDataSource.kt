package com.ataglance.walletglance.personalization.data.remote.source

import com.glanci.personalization.shared.dto.WidgetDto
import com.glanci.request.shared.ResultData
import com.glanci.request.shared.SimpleResult
import com.glanci.request.shared.error.DataError

interface WidgetRemoteDataSource {

    suspend fun getUpdateTime(token: String): ResultData<Long, DataError>

    suspend fun synchronizeWidgets(
        widgets: List<WidgetDto>,
        timestamp: Long,
        token: String
    ): SimpleResult<DataError>

    suspend fun getWidgetsAfterTimestamp(
        timestamp: Long,
        token: String
    ): ResultData<List<WidgetDto>, DataError>

    suspend fun synchronizeWidgetsAndGetAfterTimestamp(
        widgets: List<WidgetDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): ResultData<List<WidgetDto>, DataError>

}