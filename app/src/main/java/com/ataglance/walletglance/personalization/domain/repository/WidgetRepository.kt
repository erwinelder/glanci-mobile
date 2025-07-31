package com.ataglance.walletglance.personalization.domain.repository

import com.ataglance.walletglance.personalization.domain.model.WidgetName
import kotlinx.coroutines.flow.Flow

interface WidgetRepository {

    suspend fun upsertWidgets(widgets: List<WidgetName>)

    suspend fun deleteAndUpsertWidgets(
        toDelete: List<WidgetName>,
        toUpsert: List<WidgetName>
    )

    suspend fun deleteAllWidgetsLocally()

    fun getAllWidgetsAsFlow(): Flow<List<WidgetName>>

    suspend fun getAllWidgets(): List<WidgetName>

}