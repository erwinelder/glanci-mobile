package com.glanci.categoryCollection.shared.service

import com.glanci.categoryCollection.shared.dto.CategoryCollectionWithAssociationsDto
import kotlinx.rpc.annotations.Rpc

@Rpc
interface CategoryCollectionService {

    suspend fun getUpdateTime(token: String): Long?

    suspend fun saveCategoryCollectionsWithAssociations(
        collections: List<CategoryCollectionWithAssociationsDto>,
        timestamp: Long,
        token: String
    )

    suspend fun getCategoryCollectionsWithAssociationsAfterTimestamp(
        timestamp: Long,
        token: String
    ): List<CategoryCollectionWithAssociationsDto>?

    suspend fun saveCategoryCollectionsWithAssociationsAndGetAfterTimestamp(
        collections: List<CategoryCollectionWithAssociationsDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): List<CategoryCollectionWithAssociationsDto>?

}