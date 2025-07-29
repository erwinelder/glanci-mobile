package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import kotlinx.coroutines.flow.Flow

interface GetCategoriesGroupedUseCase {

    fun getAsFlow(): Flow<GroupedCategoriesByType>

    suspend fun get(): GroupedCategoriesByType

}