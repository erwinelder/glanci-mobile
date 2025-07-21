package com.ataglance.walletglance.personalization.data.remote.source

import android.util.Log
import com.glanci.personalization.shared.dto.WidgetDto
import com.glanci.personalization.shared.service.WidgetService
import kotlinx.rpc.krpc.ktor.client.KtorRpcClient
import kotlinx.rpc.withService

class WidgetRemoteDataSourceImpl(
    private val service: WidgetService
) : WidgetRemoteDataSource {

    constructor(client: KtorRpcClient) : this(service = client.withService<WidgetService>())


    override suspend fun getUpdateTime(token: String): Long? {
        return runCatching {
            service.getUpdateTime(token = token)
        }.getOrNull().also { timestamp ->
            if (timestamp != null) {
                Log.d("WidgetRemoteDataSourceImpl", "getUpdateTime:" +
                        "received update time $timestamp")
            } else {
                Log.w("WidgetRemoteDataSourceImpl", "getUpdateTime:" +
                        "no update time received")
            }
        }
    }

    override suspend fun synchronizeWidgets(
        widgets: List<WidgetDto>,
        timestamp: Long,
        token: String
    ): Boolean {
        return runCatching {
            service.saveWidgets(widgets = widgets, timestamp = timestamp, token = token)
        }.isSuccess.also { success ->
            if (success) {
                Log.d("WidgetRemoteDataSourceImpl", "synchronizeWidgets: " +
                        "synchronized ${widgets.size} widgets at timestamp $timestamp")
            } else {
                Log.e("WidgetRemoteDataSourceImpl", "synchronizeWidgets: " +
                        "failed to synchronize ${widgets.size} widgets at timestamp $timestamp")
            }
        }
    }

    override suspend fun getWidgetsAfterTimestamp(timestamp: Long, token: String): List<WidgetDto>? {
        return runCatching {
            service.getWidgetsAfterTimestamp(timestamp = timestamp, token = token)
        }.getOrNull().also { widgets ->
            widgets?.forEach {
                Log.d("WidgetRemoteDataSourceImpl", "getAccountsAfterTimestamp:" +
                        "received widget: name = ${it.name}, orderNum = ${it.orderNum}")
            }
        }
    }

    override suspend fun synchronizeWidgetsAndGetAfterTimestamp(
        widgets: List<WidgetDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): List<WidgetDto>? {
        return runCatching {
            service.saveWidgetsAndGetAfterTimestamp(
                widgets = widgets,
                timestamp = timestamp,
                localTimestamp = localTimestamp,
                token = token
            )
        }.getOrNull().also { widgets ->
            Log.d("WidgetRemoteDataSourceImpl", "synchronizeWidgetsAndGetAfterTimestamp:" +
                    "synchronized ${widgets?.size ?: 0} widgets at timestamp $timestamp" +
                    " with local timestamp $localTimestamp")
            widgets?.forEach {
                Log.d("WidgetRemoteDataSourceImpl", "synchronizeWidgetsAndGetAfterTimestamp:" +
                        "received widget: name = ${it.name}, orderNum = ${it.orderNum}")
            }
        }
    }

}