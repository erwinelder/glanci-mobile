package com.glanci.personalization.shared.service

import com.glanci.personalization.shared.dto.WidgetDto
import kotlinx.rpc.annotations.Rpc

@Rpc
interface WidgetService {

    suspend fun getUpdateTime(token: String): Long?

    suspend fun saveWidgets(widgets: List<WidgetDto>, timestamp: Long, token: String)

    suspend fun getWidgetsAfterTimestamp(timestamp: Long, token: String): List<WidgetDto>?

    suspend fun saveWidgetsAndGetAfterTimestamp(
        widgets: List<WidgetDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): List<WidgetDto>?

}