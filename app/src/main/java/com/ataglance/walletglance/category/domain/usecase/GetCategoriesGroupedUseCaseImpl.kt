package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.category.domain.mapper.groupByType
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCategoriesGroupedUseCaseImpl(
    private val categoryRepository: CategoryRepository
) : GetCategoriesGroupedUseCase {

    override fun getAsFlow(): Flow<GroupedCategoriesByType> {
        return categoryRepository.getAllCategoriesAsFlow().map { categories ->
            categories.groupByType()
        }
    }

    override suspend fun get(): GroupedCategoriesByType {
        return categoryRepository.getAllCategories().groupByType()
    }

}