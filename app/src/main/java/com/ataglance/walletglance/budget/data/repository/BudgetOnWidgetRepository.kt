package com.ataglance.walletglance.budget.data.repository

import kotlinx.coroutines.flow.Flow

interface BudgetOnWidgetRepository {

    suspend fun deleteAndUpsertBudgetsOnWidget(toDelete: List<Int>, toUpsert: List<Int>)

    fun getAllBudgetsOnWidgetAsFlow(): Flow<List<Int>>

    suspend fun getAllBudgetsOnWidget(): List<Int>

}