package com.ataglance.walletglance.categoryCollection.domain.model

data class CategoryCollection(
    val id: Int,
    val orderNum: Int,
    val type: CategoryCollectionType,
    val name: String
)
