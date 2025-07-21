package com.ataglance.walletglance.categoryCollection.data.model

data class CategoryCollectionWithAssociationsDataModel(
    val collection: CategoryCollectionDataModel,
    val associations: List<CategoryCollectionCategoryAssociationDataModel>
) {

    val collectionId: Int
        get() = collection.id

}