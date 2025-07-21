package com.ataglance.walletglance.categoryCollection.data.mapper

import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionCategoryAssociationEntity
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionEntity
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionEntityWithAssociations
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionCategoryAssociationDataModel
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionDataModel
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionWithAssociationsDataModel
import com.glanci.categoryCollection.shared.dto.CategoryCollectionCategoryAssociationDto
import com.glanci.categoryCollection.shared.dto.CategoryCollectionDto
import com.glanci.categoryCollection.shared.dto.CategoryCollectionWithAssociationsDto


fun CategoryCollectionDataModel.withAssociations(
    associations: List<CategoryCollectionCategoryAssociationDataModel> = emptyList()
): CategoryCollectionWithAssociationsDataModel {
    return CategoryCollectionWithAssociationsDataModel(
        collection = this,
        associations = associations
    )
}


fun CategoryCollectionDataModel.toEntity(
    timestamp: Long,
    deleted: Boolean
): CategoryCollectionEntity {
    return CategoryCollectionEntity(
        id = id,
        orderNum = orderNum,
        type = type,
        name = name,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun CategoryCollectionCategoryAssociationDataModel.toEntity(
): CategoryCollectionCategoryAssociationEntity {
    return CategoryCollectionCategoryAssociationEntity(
        collectionId = collectionId,
        categoryId = categoryId
    )
}

fun CategoryCollectionWithAssociationsDataModel.toEntityWithAssociations(
    timestamp: Long,
    deleted: Boolean
): CategoryCollectionEntityWithAssociations {
    return CategoryCollectionEntityWithAssociations(
        collection = collection.toEntity(timestamp = timestamp, deleted = deleted),
        associations = associations.map { it.toEntity() }
    )
}


fun CategoryCollectionEntity.toDataModel(): CategoryCollectionDataModel {
    return CategoryCollectionDataModel(
        id = id,
        orderNum = orderNum,
        type = type,
        name = name
    )
}

fun CategoryCollectionCategoryAssociationEntity.toDataModel(
): CategoryCollectionCategoryAssociationDataModel {
    return CategoryCollectionCategoryAssociationDataModel(
        collectionId = collectionId,
        categoryId = categoryId
    )
}

fun CategoryCollectionEntityWithAssociations.toDataModelWithAssociations(
): CategoryCollectionWithAssociationsDataModel {
    return CategoryCollectionWithAssociationsDataModel(
        collection = collection.toDataModel(),
        associations = associations.map { it.toDataModel() }
    )
}


fun CategoryCollectionDataModel.toDto(timestamp: Long, deleted: Boolean): CategoryCollectionDto {
    return CategoryCollectionDto(
        id = id,
        orderNum = orderNum,
        type = type,
        name = name,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun CategoryCollectionCategoryAssociationDataModel.toDto(
): CategoryCollectionCategoryAssociationDto {
    return CategoryCollectionCategoryAssociationDto(
        collectionId = collectionId,
        categoryId = categoryId
    )
}

fun CategoryCollectionWithAssociationsDataModel.toDtoWithAssociations(
    timestamp: Long,
    deleted: Boolean
): CategoryCollectionWithAssociationsDto {
    return CategoryCollectionWithAssociationsDto(
        collection = collection.toDto(timestamp = timestamp, deleted = deleted),
        associations = associations.map { it.toDto() }
    )
}


fun CategoryCollectionEntity.toDto(): CategoryCollectionDto {
    return CategoryCollectionDto(
        id = id,
        orderNum = orderNum,
        type = type,
        name = name,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun CategoryCollectionCategoryAssociationEntity.toDto(
): CategoryCollectionCategoryAssociationDto {
    return CategoryCollectionCategoryAssociationDto(
        collectionId = collectionId,
        categoryId = categoryId
    )
}

fun CategoryCollectionEntityWithAssociations.toDtoWithAssociations(
): CategoryCollectionWithAssociationsDto {
    return CategoryCollectionWithAssociationsDto(
        collection = collection.toDto(),
        associations = associations.map { it.toDto() }
    )
}


fun CategoryCollectionDto.toEntity(): CategoryCollectionEntity {
    return CategoryCollectionEntity(
        id = id,
        orderNum = orderNum,
        type = type,
        name = name,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun CategoryCollectionCategoryAssociationDto.toEntity(
): CategoryCollectionCategoryAssociationEntity {
    return CategoryCollectionCategoryAssociationEntity(
        collectionId = collectionId,
        categoryId = categoryId
    )
}

fun CategoryCollectionWithAssociationsDto.toEntityWithAssociations(
): CategoryCollectionEntityWithAssociations {
    return CategoryCollectionEntityWithAssociations(
        collection = collection.toEntity(),
        associations = associations.map { it.toEntity() }
    )
}
