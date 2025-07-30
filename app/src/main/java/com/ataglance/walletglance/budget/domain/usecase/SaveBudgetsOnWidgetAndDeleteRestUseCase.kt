package com.ataglance.walletglance.budget.domain.usecase

interface SaveBudgetsOnWidgetAndDeleteRestUseCase {

    suspend fun execute(budgetIds: List<Int>)

}