package com.ataglance.walletglance.categoryCollection.data.remote.source

import com.glanci.categoryCollection.shared.dto.CategoryCollectionWithAssociationsDto
import com.glanci.request.shared.ResultData
import com.glanci.request.shared.SimpleResult
import com.glanci.request.shared.error.DataError

interface CategoryCollectionRemoteDataSource {

    suspend fun getUpdateTime(token: String): ResultData<Long, DataError>

    suspend fun synchronizeCollectionsWithAssociations(
        collections: List<CategoryCollectionWithAssociationsDto>,
        timestamp: Long,
        token: String
    ): SimpleResult<DataError>

    suspend fun getCollectionsWithAssociationsAfterTimestamp(
        timestamp: Long,
        token: String
    ): ResultData<List<CategoryCollectionWithAssociationsDto>, DataError>

    suspend fun synchronizeCollectionsWithAssociationsAndGetAfterTimestamp(
        collections: List<CategoryCollectionWithAssociationsDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): ResultData<List<CategoryCollectionWithAssociationsDto>, DataError>

}