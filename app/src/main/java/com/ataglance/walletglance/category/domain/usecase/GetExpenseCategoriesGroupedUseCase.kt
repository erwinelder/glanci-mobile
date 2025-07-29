package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.category.domain.model.GroupedCategories

interface GetExpenseCategoriesGroupedUseCase {

    suspend fun execute(): List<GroupedCategories>

}