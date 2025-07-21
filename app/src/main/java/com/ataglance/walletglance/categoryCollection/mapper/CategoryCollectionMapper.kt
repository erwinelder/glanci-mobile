package com.ataglance.walletglance.categoryCollection.mapper

import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionCategoryAssociationDataModel
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionDataModel
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionWithAssociationsDataModel
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithCategories
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithIds
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionsWithIdsByType
import com.ataglance.walletglance.categoryCollection.domain.utils.asCategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.utils.asChar


fun CategoryCollectionWithIds.toDataModel(): CategoryCollectionDataModel {
    return CategoryCollectionDataModel(
        id = id,
        orderNum = orderNum,
        type = type.asChar(),
        name = name
    )
}

fun CategoryCollectionWithIds.toDataModelWithAssociations(
): CategoryCollectionWithAssociationsDataModel? {
    val categoryIds = categoryIds ?: return null

    return CategoryCollectionWithAssociationsDataModel(
        collection = toDataModel(),
        associations = categoryIds.map { categoryId ->
            CategoryCollectionCategoryAssociationDataModel(
                collectionId = id, categoryId = categoryId
            )
        }
    )
}

fun CategoryCollectionWithCategories.toDataModelWithAssociations(
): CategoryCollectionWithAssociationsDataModel? {
    return toCollectionWithIds().toDataModelWithAssociations()
}


fun CategoryCollectionWithAssociationsDataModel.toDomainModelWithIds(): CategoryCollectionWithIds {
    return CategoryCollectionWithIds(
        id = collection.id,
        orderNum = collection.orderNum,
        type = collection.type.asCategoryCollectionType(),
        name = collection.name,
        categoryIds = associations.map { it.categoryId }
    )
}

fun List<CategoryCollectionWithAssociationsDataModel>.groupByType(
): CategoryCollectionsWithIdsByType {
    return CategoryCollectionsWithIdsByType.fromCollections(
        collections = map { it.toDomainModelWithIds() }
    )
}
