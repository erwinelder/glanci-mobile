package com.ataglance.walletglance.navigation.data.remote.source

import android.util.Log
import com.glanci.navigation.shared.dto.NavigationButtonDto
import com.glanci.navigation.shared.service.NavigationButtonService
import kotlinx.rpc.krpc.ktor.client.KtorRpcClient
import kotlinx.rpc.withService

class NavigationButtonRemoteDataSourceImpl(
    private val service: NavigationButtonService
) : NavigationButtonRemoteDataSource {

    constructor(client: KtorRpcClient) : this(
        service = client.withService<NavigationButtonService>()
    )


    override suspend fun getUpdateTime(token: String): Long? {
        return runCatching {
            service.getUpdateTime(token = token)
        }.getOrNull().also { timestamp ->
            if (timestamp != null) {
                Log.d("NavigationButtonRemoteDataSourceImpl", "getUpdateTime:" +
                        "received update time $timestamp")
            } else {
                Log.w("NavigationButtonRemoteDataSourceImpl", "getUpdateTime:" +
                        "no update time received")
            }
        }
    }

    override suspend fun synchronizeNavigationButtons(
        buttons: List<NavigationButtonDto>,
        timestamp: Long,
        token: String
    ): Boolean {
        return runCatching {
            service.saveNavigationButtons(buttons = buttons, timestamp = timestamp, token = token)
        }.isSuccess.also { success ->
            if (success) {
                Log.d("NavigationButtonRemoteDataSourceImpl", "synchronizeNavigationButtons: " +
                        "synchronized ${buttons.size} buttons at timestamp $timestamp")
            } else {
                Log.e("NavigationButtonRemoteDataSourceImpl", "synchronizeNavigationButtons: " +
                        "failed to synchronize ${buttons.size} buttons at timestamp $timestamp")
            }
        }
    }

    override suspend fun getNavigationButtonsAfterTimestamp(
        timestamp: Long,
        token: String
    ): List<NavigationButtonDto>? {
        return runCatching {
            service.getNavigationButtonsAfterTimestamp(timestamp = timestamp, token = token)
        }.getOrNull().also { buttons ->
            buttons?.forEach {
                Log.d("NavigationButtonRemoteDataSourceImpl", "getNavigationButtonsAfterTimestamp:" +
                        "received navigation button: screenName = ${it.screenName}, orderNum = ${it.orderNum}")
            }
        }
    }

    override suspend fun synchronizeNavigationButtonsAndGetAfterTimestamp(
        buttons: List<NavigationButtonDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): List<NavigationButtonDto>? {
        return runCatching {
            service.saveNavigationButtonsAndGetAfterTimestamp(
                buttons = buttons,
                timestamp = timestamp,
                localTimestamp = localTimestamp,
                token = token
            )
        }.getOrNull().also { buttons ->
            Log.d("NavigationButtonRemoteDataSourceImpl", "synchronizeNavigationButtonsAndGetAfterTimestamp:" +
                    "synchronized ${buttons?.size ?: 0} buttons at timestamp $timestamp" +
                    " with local timestamp $localTimestamp")
            buttons?.forEach {
                Log.d("NavigationButtonRemoteDataSourceImpl", "synchronizeNavigationButtonsAndGetAfterTimestamp:" +
                        "received navigation button: screenName = ${it.screenName}, orderNum = ${it.orderNum}")
            }
        }
    }

}