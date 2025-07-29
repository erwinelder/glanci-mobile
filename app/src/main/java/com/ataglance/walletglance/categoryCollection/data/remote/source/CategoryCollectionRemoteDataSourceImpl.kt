package com.ataglance.walletglance.categoryCollection.data.remote.source

import com.glanci.categoryCollection.shared.dto.CategoryCollectionWithAssociationsDto
import com.glanci.categoryCollection.shared.service.CategoryCollectionService
import com.glanci.request.shared.ResultData
import com.glanci.request.shared.SimpleResult
import com.glanci.request.shared.error.CategoryCollectionDataError
import com.glanci.request.shared.error.DataError
import kotlinx.rpc.krpc.ktor.client.KtorRpcClient
import kotlinx.rpc.withService

class CategoryCollectionRemoteDataSourceImpl(
    private val service: CategoryCollectionService
) : CategoryCollectionRemoteDataSource {

    constructor(client: KtorRpcClient) : this(
        service = client.withService<CategoryCollectionService>()
    )


    override suspend fun getUpdateTime(token: String): ResultData<Long, DataError> {
        return runCatching {
            service.getUpdateTime(token = token)
        }.getOrDefault(
            defaultValue = ResultData.Error(
                error = CategoryCollectionDataError.CategoryCollectionError
            )
        )
    }

    override suspend fun synchronizeCollectionsWithAssociations(
        collections: List<CategoryCollectionWithAssociationsDto>,
        timestamp: Long,
        token: String
    ): SimpleResult<DataError> {
        return runCatching {
            service.saveCategoryCollectionsWithAssociations(
                collections = collections, timestamp = timestamp, token = token
            )
        }.getOrDefault(
            defaultValue = SimpleResult.Error(
                error = CategoryCollectionDataError.CategoryCollectionError
            )
        )
    }

    override suspend fun getCollectionsWithAssociationsAfterTimestamp(
        timestamp: Long,
        token: String
    ): ResultData<List<CategoryCollectionWithAssociationsDto>, DataError> {
        return runCatching {
            service.getCategoryCollectionsWithAssociationsAfterTimestamp(
                timestamp = timestamp, token = token
            )
        }.getOrDefault(
            defaultValue = ResultData.Error(
                error = CategoryCollectionDataError.CategoryCollectionError
            )
        )
    }

    override suspend fun synchronizeCollectionsWithAssociationsAndGetAfterTimestamp(
        collections: List<CategoryCollectionWithAssociationsDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): ResultData<List<CategoryCollectionWithAssociationsDto>, DataError> {
        return runCatching {
            service.saveCategoryCollectionsWithAssociationsAndGetAfterTimestamp(
                collections = collections,
                timestamp = timestamp,
                localTimestamp = localTimestamp,
                token = token
            )
        }.getOrDefault(
            defaultValue = ResultData.Error(
                error = CategoryCollectionDataError.CategoryCollectionError
            )
        )
    }

}