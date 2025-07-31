package com.ataglance.walletglance.personalization.domain.usecase.widgets

import com.ataglance.walletglance.personalization.domain.model.WidgetName

interface SaveWidgetsAndDeleteRestUseCase {

    suspend fun execute(widgets: List<WidgetName>)

}