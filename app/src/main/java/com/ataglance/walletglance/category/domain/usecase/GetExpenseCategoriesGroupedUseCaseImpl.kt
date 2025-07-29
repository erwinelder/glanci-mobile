package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.category.domain.mapper.group
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.GroupedCategories
import com.ataglance.walletglance.category.domain.repository.CategoryRepository

class GetExpenseCategoriesGroupedUseCaseImpl(
    private val categoryRepository: CategoryRepository
) : GetExpenseCategoriesGroupedUseCase {

    override suspend fun execute(): List<GroupedCategories> {
        return categoryRepository.getCategoriesByType(type = CategoryType.Expense.asChar()).group()
    }

}