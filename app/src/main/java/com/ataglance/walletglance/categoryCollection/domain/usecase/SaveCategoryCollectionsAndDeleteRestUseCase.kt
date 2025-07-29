package com.ataglance.walletglance.categoryCollection.domain.usecase

import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithCategories

interface SaveCategoryCollectionsAndDeleteRestUseCase {
    suspend fun execute(collections: List<CategoryCollectionWithCategories>)
}