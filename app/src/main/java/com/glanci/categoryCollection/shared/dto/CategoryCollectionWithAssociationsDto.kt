package com.glanci.categoryCollection.shared.dto

import kotlinx.serialization.Serializable

@Serializable
data class CategoryCollectionWithAssociationsDto(
    val collection: CategoryCollectionDto,
    val associations: List<CategoryCollectionCategoryAssociationDto>
)
