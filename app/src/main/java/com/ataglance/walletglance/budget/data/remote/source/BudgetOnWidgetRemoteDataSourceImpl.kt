package com.ataglance.walletglance.budget.data.remote.source

import android.util.Log
import com.glanci.budget.shared.dto.BudgetOnWidgetDto
import com.glanci.budget.shared.service.BudgetOnWidgetService
import kotlinx.rpc.krpc.ktor.client.KtorRpcClient
import kotlinx.rpc.withService

class BudgetOnWidgetRemoteDataSourceImpl(
    private val service: BudgetOnWidgetService
) : BudgetOnWidgetRemoteDataSource {

    constructor(client: KtorRpcClient) : this(service = client.withService<BudgetOnWidgetService>())


    override suspend fun getUpdateTime(token: String): Long? {
        return runCatching {
            service.getUpdateTime(token = token)
        }.getOrNull().also { timestamp ->
            if (timestamp != null) {
                Log.d("BudgetOnWidgetRemoteDataSourceImpl", "getUpdateTime:" +
                        "received update time $timestamp")
            } else {
                Log.w("BudgetOnWidgetRemoteDataSourceImpl", "getUpdateTime:" +
                        "no update time received")
            }
        }
    }

    override suspend fun synchronizeBudgetsOnWidget(
        budgets: List<BudgetOnWidgetDto>,
        timestamp: Long,
        token: String
    ): Boolean {
        return runCatching {
            service.saveBudgetsOnWidget(budgets = budgets, timestamp = timestamp, token = token)
        }.isSuccess.also { success ->
            if (success) {
                Log.d("BudgetOnWidgetRemoteDataSourceImpl", "synchronizeBudgetsOnWidget: " +
                        "synchronized ${budgets.size} budgets at timestamp $timestamp")
            } else {
                Log.e("BudgetOnWidgetRemoteDataSourceImpl", "synchronizeBudgetsOnWidget: " +
                        "failed to synchronize ${budgets.size} budgets at timestamp $timestamp")
            }
        }
    }

    override suspend fun getBudgetsOnWidgetAfterTimestamp(
        timestamp: Long,
        token: String
    ): List<BudgetOnWidgetDto>? {
        return runCatching {
            service.getBudgetsOnWidgetAfterTimestamp(timestamp = timestamp, token = token)
        }.getOrNull().also { budgets ->
            budgets?.forEach {
                Log.d("BudgetOnWidgetRemoteDataSourceImpl", "getBudgetsOnWidgetAfterTimestamp:" +
                        "received budget on widget: budgetId = ${it.budgetId}")
            }
        }
    }

    override suspend fun synchronizeBudgetsOnWidgetAndGetAfterTimestamp(
        budgets: List<BudgetOnWidgetDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): List<BudgetOnWidgetDto>? {
        return runCatching {
            service.saveBudgetsOnWidgetAndGetAfterTimestamp(
                budgets = budgets,
                timestamp = timestamp,
                localTimestamp = localTimestamp,
                token = token
            )
        }.getOrNull().also { budgets ->
            Log.d("BudgetOnWidgetRemoteDataSourceImpl", "synchronizeBudgetsOnWidgetAndGetAfterTimestamp:" +
                    "synchronized ${budgets?.size ?: 0} budgets at timestamp $timestamp" +
                    " with local timestamp $localTimestamp")
            budgets?.forEach {
                Log.d("BudgetOnWidgetRemoteDataSourceImpl", "synchronizeBudgetsOnWidgetAndGetAfterTimestamp:" +
                        "received budget on widget: budgetId = ${it.budgetId}")
            }
        }
    }

}