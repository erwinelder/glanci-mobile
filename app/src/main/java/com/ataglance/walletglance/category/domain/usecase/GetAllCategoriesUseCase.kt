package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.category.domain.model.Category

interface GetAllCategoriesUseCase {

    suspend fun execute(): List<Category>

}