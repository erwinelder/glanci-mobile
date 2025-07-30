package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.domain.model.GroupedFilledBudgets
import kotlinx.coroutines.flow.Flow

interface GetGroupedFilledBudgetsUseCase {

    fun getAsFlow(): Flow<GroupedFilledBudgets>

}