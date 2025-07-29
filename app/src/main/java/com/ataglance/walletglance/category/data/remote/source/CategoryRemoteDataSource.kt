package com.ataglance.walletglance.category.data.remote.source

import com.glanci.category.shared.dto.CategoryCommandDto
import com.glanci.category.shared.dto.CategoryQueryDto
import com.glanci.request.shared.ResultData
import com.glanci.request.shared.SimpleResult
import com.glanci.request.shared.error.DataError

interface CategoryRemoteDataSource {

    suspend fun getUpdateTime(token: String): ResultData<Long, DataError>

    suspend fun synchronizeCategories(
        categories: List<CategoryCommandDto>,
        timestamp: Long,
        token: String
    ): SimpleResult<DataError>

    suspend fun getCategoriesAfterTimestamp(
        timestamp: Long,
        token: String
    ): ResultData<List<CategoryQueryDto>, DataError>

    suspend fun synchronizeCategoriesAndGetAfterTimestamp(
        categories: List<CategoryCommandDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): ResultData<List<CategoryQueryDto>, DataError>

}
