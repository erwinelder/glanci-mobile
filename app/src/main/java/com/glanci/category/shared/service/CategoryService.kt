package com.glanci.category.shared.service

import com.glanci.category.shared.dto.CategoryCommandDto
import com.glanci.category.shared.dto.CategoryQueryDto
import com.glanci.request.shared.ResultData
import com.glanci.request.shared.SimpleResult
import com.glanci.request.shared.error.DataError
import kotlinx.rpc.annotations.Rpc

@Rpc
interface CategoryService {

    suspend fun getUpdateTime(token: String): ResultData<Long, DataError>

    suspend fun saveCategories(
        categories: List<CategoryCommandDto>,
        timestamp: Long,
        token: String
    ): SimpleResult<DataError>

    suspend fun getCategoriesAfterTimestamp(
        timestamp: Long,
        token: String
    ): ResultData<List<CategoryQueryDto>, DataError>

    suspend fun saveCategoriesAndGetAfterTimestamp(
        categories: List<CategoryCommandDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): ResultData<List<CategoryQueryDto>, DataError>

}