package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.domain.model.GroupedBudgets
import kotlinx.coroutines.flow.Flow

interface GetGroupedBudgetsUseCase {

    fun getAsFlow(): Flow<GroupedBudgets>

}