package com.glanci.categoryCollection.shared.service

import com.glanci.categoryCollection.shared.dto.CategoryCollectionWithAssociationsDto
import com.glanci.request.shared.ResultData
import com.glanci.request.shared.SimpleResult
import com.glanci.request.shared.error.DataError
import kotlinx.rpc.annotations.Rpc

@Rpc
interface CategoryCollectionService {

    suspend fun getUpdateTime(token: String): ResultData<Long, DataError>

    suspend fun saveCategoryCollectionsWithAssociations(
        collections: List<CategoryCollectionWithAssociationsDto>,
        timestamp: Long,
        token: String
    ): SimpleResult<DataError>

    suspend fun getCategoryCollectionsWithAssociationsAfterTimestamp(
        timestamp: Long,
        token: String
    ): ResultData<List<CategoryCollectionWithAssociationsDto>, DataError>

    suspend fun saveCategoryCollectionsWithAssociationsAndGetAfterTimestamp(
        collections: List<CategoryCollectionWithAssociationsDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): ResultData<List<CategoryCollectionWithAssociationsDto>, DataError>

}