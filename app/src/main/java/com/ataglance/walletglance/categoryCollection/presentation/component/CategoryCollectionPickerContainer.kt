package com.ataglance.walletglance.categoryCollection.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithIds

@Composable
fun CategoryCollectionPickerContainer(
    collectionList: List<CategoryCollectionWithIds>,
    activeCollection: CategoryCollectionWithIds,
    onCollectionSelect: (Int) -> Unit,
    activeType: CategoryCollectionType,
    onTypeToggle: () -> Unit,
    onNavigateToEditCollectionsScreen: () -> Unit,
    onDimBackgroundChange: (Boolean) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.screen_horizontal_padding))
    ) {
        CategoryCollectionPicker(
            collectionList = collectionList,
            selectedCollection = activeCollection,
            onCollectionSelect = onCollectionSelect,
            onNavigateToEditCollectionsScreen = onNavigateToEditCollectionsScreen,
            onDimBackgroundChange = onDimBackgroundChange
        )
        CategoryCollectionTypeToggleButton(currentType = activeType, onClick = onTypeToggle)
    }
}