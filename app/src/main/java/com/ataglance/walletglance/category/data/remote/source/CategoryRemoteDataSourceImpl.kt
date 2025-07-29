package com.ataglance.walletglance.category.data.remote.source

import com.glanci.category.shared.dto.CategoryCommandDto
import com.glanci.category.shared.dto.CategoryQueryDto
import com.glanci.category.shared.service.CategoryService
import com.glanci.request.shared.ResultData
import com.glanci.request.shared.SimpleResult
import com.glanci.request.shared.error.CategoryDataError
import com.glanci.request.shared.error.DataError
import kotlinx.rpc.krpc.ktor.client.KtorRpcClient
import kotlinx.rpc.withService

class CategoryRemoteDataSourceImpl(
    private val service: CategoryService
) : CategoryRemoteDataSource {

    constructor(client: KtorRpcClient) : this(service = client.withService<CategoryService>())


    override suspend fun getUpdateTime(token: String): ResultData<Long, DataError> {
        return runCatching {
            service.getUpdateTime(token = token)
        }.getOrDefault(
            defaultValue = ResultData.Error(error = CategoryDataError.CategoryError)
        )
    }

    override suspend fun synchronizeCategories(
        categories: List<CategoryCommandDto>,
        timestamp: Long,
        token: String
    ): SimpleResult<DataError> {
        return runCatching {
            service.saveCategories(categories = categories, timestamp = timestamp, token = token)
        }.getOrDefault(
            defaultValue = SimpleResult.Error(error = CategoryDataError.CategoryError)
        )
    }

    override suspend fun getCategoriesAfterTimestamp(
        timestamp: Long,
        token: String
    ): ResultData<List<CategoryQueryDto>, DataError> {
        return runCatching {
            service.getCategoriesAfterTimestamp(timestamp = timestamp, token = token)
        }.getOrDefault(
            defaultValue = ResultData.Error(error = CategoryDataError.CategoryError)
        )
    }

    override suspend fun synchronizeCategoriesAndGetAfterTimestamp(
        categories: List<CategoryCommandDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): ResultData<List<CategoryQueryDto>, DataError> {
        return runCatching {
            service.saveCategoriesAndGetAfterTimestamp(
                categories = categories,
                timestamp = timestamp,
                localTimestamp = localTimestamp,
                token = token
            )
        }.getOrDefault(
            defaultValue = ResultData.Error(error = CategoryDataError.CategoryError)
        )
    }

}