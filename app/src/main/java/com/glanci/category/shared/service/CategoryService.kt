package com.glanci.category.shared.service

import com.glanci.category.shared.dto.CategoryCommandDto
import com.glanci.category.shared.dto.CategoryQueryDto
import kotlinx.rpc.annotations.Rpc

@Rpc
interface CategoryService {

    suspend fun getUpdateTime(token: String): Long?

    suspend fun saveCategories(categories: List<CategoryCommandDto>, timestamp: Long, token: String)

    suspend fun getCategoriesAfterTimestamp(timestamp: Long, token: String): List<CategoryQueryDto>?

    suspend fun saveCategoriesAndGetAfterTimestamp(
        categories: List<CategoryCommandDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): List<CategoryQueryDto>?

}