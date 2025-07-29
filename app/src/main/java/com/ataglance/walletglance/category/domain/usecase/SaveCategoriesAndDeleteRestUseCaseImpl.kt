package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.category.domain.repository.CategoryRepository
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.core.utils.excludeItems

class SaveCategoriesAndDeleteRestUseCaseImpl(
    private val categoryRepository: CategoryRepository
) : SaveCategoriesAndDeleteRestUseCase {

    override suspend fun execute(categories: List<Category>) {
        val entitiesToDelete = categoryRepository.getAllCategories()
            .excludeItems(categories) { it.id }

        if (entitiesToDelete.isEmpty()) {
            categoryRepository.upsertCategories(categories = categories)
        } else {
            categoryRepository.deleteAndUpsertCategories(
                toDelete = entitiesToDelete, toUpsert = categories
            )
        }
    }

}