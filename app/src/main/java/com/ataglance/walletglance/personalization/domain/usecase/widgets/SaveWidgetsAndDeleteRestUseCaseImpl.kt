package com.ataglance.walletglance.personalization.domain.usecase.widgets

import com.ataglance.walletglance.core.utils.excludeItems
import com.ataglance.walletglance.personalization.domain.repository.WidgetRepository
import com.ataglance.walletglance.personalization.domain.model.WidgetName

class SaveWidgetsAndDeleteRestUseCaseImpl(
    private val widgetRepository: WidgetRepository
) : SaveWidgetsAndDeleteRestUseCase {

    override suspend fun execute(widgets: List<WidgetName>) {
        val currentWidgets = widgetRepository.getAllWidgets()

        val entitiesToDelete = currentWidgets.excludeItems(widgets) { it.name }

        widgetRepository.deleteAndUpsertWidgets(toDelete = entitiesToDelete, toUpsert = widgets)
    }

}