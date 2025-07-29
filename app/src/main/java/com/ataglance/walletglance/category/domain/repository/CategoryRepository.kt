package com.ataglance.walletglance.category.domain.repository

import com.ataglance.walletglance.category.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun upsertCategories(categories: List<Category>)

    suspend fun deleteAndUpsertCategories(
        toDelete: List<Category>,
        toUpsert: List<Category>
    )

    suspend fun deleteAllCategoriesLocally()

    fun getAllCategoriesAsFlow(): Flow<List<Category>>

    suspend fun getAllCategories(): List<Category>

    suspend fun getCategoriesByType(type: Char): List<Category>

}