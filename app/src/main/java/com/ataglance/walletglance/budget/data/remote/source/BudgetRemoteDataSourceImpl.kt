package com.ataglance.walletglance.budget.data.remote.source

import android.util.Log
import com.glanci.budget.shared.dto.BudgetWithAssociationsDto
import com.glanci.budget.shared.service.BudgetService
import kotlinx.rpc.krpc.ktor.client.KtorRpcClient
import kotlinx.rpc.withService

class BudgetRemoteDataSourceImpl(
    private val service: BudgetService
) : BudgetRemoteDataSource {

    constructor(client: KtorRpcClient) : this(service = client.withService<BudgetService>())


    override suspend fun getUpdateTime(token: String): Long? {
        return runCatching {
            service.getUpdateTime(token = token)
        }.getOrNull().also { timestamp ->
            if (timestamp != null) {
                Log.d("BudgetRemoteDataSourceImpl", "getUpdateTime:" +
                        "received update time $timestamp")
            } else {
                Log.w("BudgetRemoteDataSourceImpl", "getUpdateTime:" +
                        "no update time received")
            }
        }
    }

    override suspend fun synchronizeBudgetsWithAssociations(
        budgets: List<BudgetWithAssociationsDto>,
        timestamp: Long,
        token: String
    ): Boolean {
        return runCatching {
            service.saveBudgetsWithAssociations(
                budgets = budgets,
                timestamp = timestamp,
                token = token
            )
        }.isSuccess.also { success ->
            if (success) {
                Log.d("BudgetRemoteDataSourceImpl", "synchronizeBudgetsWithAssociations: " +
                        "synchronized ${budgets.size} budgets at timestamp $timestamp")
            } else {
                Log.e("BudgetRemoteDataSourceImpl", "synchronizeBudgetsWithAssociations: " +
                        "failed to synchronize ${budgets.size} budgets at timestamp $timestamp")
            }
        }
    }

    override suspend fun getBudgetsWithAssociationsAfterTimestamp(
        timestamp: Long,
        token: String
    ): List<BudgetWithAssociationsDto>? {
        return runCatching {
            service.getBudgetsWithAssociationsAfterTimestamp(timestamp = timestamp, token = token)
        }.getOrNull().also { budgets ->
            budgets?.forEach {
                Log.d("BudgetRemoteDataSourceImpl", "getBudgetsWithAssociationsAfterTimestamp:" +
                        "received budget: id = ${it.budget.id}")
            }
        }
    }

    override suspend fun synchronizeBudgetsWithAssociationsAndGetAfterTimestamp(
        budgets: List<BudgetWithAssociationsDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): List<BudgetWithAssociationsDto>? {
        return runCatching {
            service.saveBudgetsWithAssociationsAndGetAfterTimestamp(
                budgets = budgets,
                timestamp = timestamp,
                localTimestamp = localTimestamp,
                token = token
            )
        }.getOrNull().also { budgets ->
            Log.d("BudgetRemoteDataSourceImpl", "synchronizeBudgetsWithAssociationsAndGetAfterTimestamp:" +
                    "synchronized ${budgets?.size ?: 0} budgets at timestamp $timestamp" +
                    " with local timestamp $localTimestamp")
            budgets?.forEach {
                Log.d("BudgetRemoteDataSourceImpl", "synchronizeBudgetsWithAssociationsAndGetAfterTimestamp:" +
                        "received budget: id = ${it.budget.id}")
            }
        }
    }

}