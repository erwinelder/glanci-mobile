package com.glanci.categoryCollection.shared.dto

import kotlinx.serialization.Serializable

@Serializable
data class CategoryCollectionCategoryAssociationDto(
    val collectionId: Int,
    val categoryId: Int
)