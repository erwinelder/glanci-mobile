package com.ataglance.walletglance.categoryCollection.domain.usecase

import com.ataglance.walletglance.categoryCollection.domain.repository.CategoryCollectionRepository
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithCategories
import com.ataglance.walletglance.core.utils.excludeItems

class SaveCategoryCollectionsAndDeleteRestUseCaseImpl(
    private val categoryCollectionRepository: CategoryCollectionRepository
) : SaveCategoryCollectionsAndDeleteRestUseCase {

    override suspend fun execute(collections: List<CategoryCollectionWithCategories>) {
        val currentCollections = categoryCollectionRepository.getAllCollections()

        val collectionsToUpsert = collections.map { it.toCollectionWithIds() }
        val collectionsToDelete = currentCollections.excludeItems(
            items = collectionsToUpsert,
            keySelector1 = { it.id },
            keySelector2 = { it.id }
        )

        categoryCollectionRepository.deleteAndUpsertCollectionsWithAssociations(
            toDelete = collectionsToDelete,
            toUpsert = collectionsToUpsert
        )
    }

}