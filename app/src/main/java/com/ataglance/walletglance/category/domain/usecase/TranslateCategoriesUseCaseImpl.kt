package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.repository.CategoryRepository
import com.ataglance.walletglance.category.domain.utils.translateCategories

class TranslateCategoriesUseCaseImpl(
    private val categoryRepository: CategoryRepository
) : TranslateCategoriesUseCase {
    override suspend fun execute(
        defaultCategoriesInCurrLocale: List<Category>,
        defaultCategoriesInNewLocale: List<Category>
    ) {
        val currCategories = categoryRepository.getAllCategories()
        val translatedCategories = translateCategories(
            defaultCategoriesInCurrLocale = defaultCategoriesInCurrLocale,
            defaultCategoriesInNewLocale = defaultCategoriesInNewLocale,
            currCategoryList = currCategories
        )
        categoryRepository.upsertCategories(categories = translatedCategories)
    }
}