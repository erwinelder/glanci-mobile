package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.repository.CategoryRepository

class GetAllCategoriesUseCaseImpl(
    private val categoryRepository: CategoryRepository
) : GetAllCategoriesUseCase {

    override suspend fun execute(): List<Category> {
        return categoryRepository.getAllCategories()
    }

}