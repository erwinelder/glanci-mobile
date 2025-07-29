package com.ataglance.walletglance.categoryCollection.domain.repository

import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollection
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithIds
import kotlinx.coroutines.flow.Flow

interface CategoryCollectionRepository {

    suspend fun deleteAllCollectionsLocally()

    suspend fun deleteAndUpsertCollectionsWithAssociations(
        toDelete: List<CategoryCollection>,
        toUpsert: List<CategoryCollectionWithIds>
    )

    suspend fun getAllCollections(): List<CategoryCollection>

    fun getAllCollectionsWithAssociationsAsFlow(): Flow<List<CategoryCollectionWithIds>>

    suspend fun getAllCollectionsWithAssociations(): List<CategoryCollectionWithIds>

}