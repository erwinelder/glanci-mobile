package com.ataglance.walletglance.personalization.domain.usecase.widgets

import com.ataglance.walletglance.personalization.domain.repository.WidgetRepository
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class GetWidgetsUseCaseImpl(
    private val widgetRepository: WidgetRepository
) : GetWidgetsUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAsFlow(): Flow<List<WidgetName>> {
        return widgetRepository.getAllWidgetsAsFlow().mapLatest { widgets ->
            widgets.ifEmpty {
                getDefaultWidgetNames().also { widgetRepository.upsertWidgets(widgets = it) }
            }
        }
    }

    private fun getDefaultWidgetNames(): List<WidgetName> {
        return listOf(
            WidgetName.ChosenBudgets,
            WidgetName.TotalForPeriod,
            WidgetName.RecentRecords,
            WidgetName.TopExpenseCategories
        )
    }

}