package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.category.domain.model.Category

interface SaveCategoriesAndDeleteRestUseCase {

    suspend fun execute(categories: List<Category>)

}