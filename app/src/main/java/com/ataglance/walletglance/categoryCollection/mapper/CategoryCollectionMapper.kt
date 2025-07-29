package com.ataglance.walletglance.categoryCollection.mapper

import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionCategoryAssociationDataModel
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionDataModel
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionWithAssociationsDataModel
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollection
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithIds


fun CategoryCollectionDataModel.toDomainModel(): CategoryCollection {
    return CategoryCollection(
        id = id,
        orderNum = orderNum,
        type = CategoryCollectionType.fromChar(char = type),
        name = name
    )
}


fun CategoryCollection.toDataModel(): CategoryCollectionDataModel {
    return CategoryCollectionDataModel(
        id = id,
        orderNum = orderNum,
        type = type.asChar(),
        name = name
    )
}

fun CategoryCollection.toDataModelWithAssociations(
    associations: List<CategoryCollectionCategoryAssociationDataModel> = emptyList()
): CategoryCollectionWithAssociationsDataModel {
    return CategoryCollectionWithAssociationsDataModel(
        collection = this.toDataModel(),
        associations = associations
    )
}


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


fun CategoryCollectionWithAssociationsDataModel.toDomainModelWithIds(): CategoryCollectionWithIds {
    return CategoryCollectionWithIds(
        id = collection.id,
        orderNum = collection.orderNum,
        type = CategoryCollectionType.fromChar(char = collection.type),
        name = collection.name,
        categoryIds = associations.map { it.categoryId }
    )
}
