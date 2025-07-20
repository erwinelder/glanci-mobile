package com.ataglance.walletglance.category.data.remote.source

import com.glanci.category.shared.dto.CategoryCommandDto
import com.glanci.category.shared.dto.CategoryQueryDto

interface CategoryRemoteDataSource {

    suspend fun getUpdateTime(token: String): Long?

    suspend fun synchronizeCategories(
        categories: List<CategoryCommandDto>,
        timestamp: Long,
        token: String
    ): Boolean

    suspend fun getCategoriesAfterTimestamp(timestamp: Long, token: String): List<CategoryQueryDto>?

    suspend fun synchronizeCategoriesAndGetAfterTimestamp(
        categories: List<CategoryCommandDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): List<CategoryQueryDto>?

}
