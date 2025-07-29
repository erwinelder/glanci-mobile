package com.ataglance.walletglance.categoryCollection.domain.usecase

import com.ataglance.walletglance.categoryCollection.domain.repository.CategoryCollectionRepository
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionsWithIdsByType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCategoryCollectionsGroupedUseCaseImpl(
    private val categoryCollectionRepository: CategoryCollectionRepository
) : GetCategoryCollectionsGroupedUseCase {

    override fun getAsFlow(): Flow<CategoryCollectionsWithIdsByType> {
        return categoryCollectionRepository
            .getAllCollectionsWithAssociationsAsFlow()
            .map { CategoryCollectionsWithIdsByType.fromCollections(collections = it) }
    }

    override suspend fun get(): CategoryCollectionsWithIdsByType {
        return categoryCollectionRepository.getAllCollectionsWithAssociations().let {
            CategoryCollectionsWithIdsByType.fromCollections(collections = it)
        }
    }

}