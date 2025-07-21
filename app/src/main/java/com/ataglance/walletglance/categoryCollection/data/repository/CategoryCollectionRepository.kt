package com.ataglance.walletglance.categoryCollection.data.repository

import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionDataModel
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionWithAssociationsDataModel
import kotlinx.coroutines.flow.Flow

interface CategoryCollectionRepository {

    suspend fun deleteAllCollectionsLocally()

    suspend fun deleteAndUpsertCollectionsWithAssociations(
        toDelete: List<CategoryCollectionDataModel>,
        toUpsert: List<CategoryCollectionWithAssociationsDataModel>
    )

    suspend fun getAllCollections(): List<CategoryCollectionDataModel>

    fun getAllCollectionsWithAssociationsAsFlow(
    ): Flow<List<CategoryCollectionWithAssociationsDataModel>>

    suspend fun getAllCollectionsWithAssociations(
    ): List<CategoryCollectionWithAssociationsDataModel>

}