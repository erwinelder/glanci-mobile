package com.ataglance.walletglance.core.presentation.component.checkbox

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryColor
import com.ataglance.walletglance.category.domain.model.CategoryIcon
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.mapper.toCheckedCategoriesWithSubcategories
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithCategories
import com.ataglance.walletglance.categoryCollection.presentation.screen.EditCategoryCollectionScreen
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.preview.PreviewContainer
import com.ataglance.walletglance.core.presentation.theme.GlanciColors

@Composable
fun ThreeStateCheckbox(
    state: Boolean?,
    size: Dp = 28.dp,
    onClick: (Boolean?) -> Unit
) {
    val iconRes = when (state) {
        true -> R.drawable.checked_icon
        false -> null
        else -> R.drawable.partly_checked_icon
    }
    val checkboxBackground by animateColorAsState(
        targetValue = if (state == false) GlanciColors.glassGradientPair.first else
            GlanciColors.primaryGlassGradientPair.first,
        animationSpec = tween(150)
    )
    val checkboxBorderColor by animateColorAsState(
        targetValue = if (state == false) GlanciColors.outlineSemiTransparent else Color.Transparent,
        animationSpec = tween(150)
    )

    FilledIconButton(
        onClick = {
            onClick(state?.let { !state } ?: true)
        },
        shape = RoundedCornerShape(24),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = checkboxBackground
        ),
        modifier = Modifier
            .border(1.dp, checkboxBorderColor, RoundedCornerShape(24))
            .size(size)
    ) {
        AnimatedContent(
            targetState = iconRes,
            label = "three state checkbox icon",
            transitionSpec = {
                (fadeIn(tween(150, 70)) +
                        scaleIn(tween(150, 70), .92f))
                    .togetherWith(fadeOut(tween(70)))
            },
            contentAlignment = Alignment.Center
        ) { targetIconRes ->
            targetIconRes?.let {
                Icon(
                    painter = painterResource(targetIconRes),
                    contentDescription = "",
                    tint = GlanciColors.background,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}



@Preview(device = PIXEL_7_PRO)
@Composable
private fun ThreeStateCheckboxPreview() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ThreeStateCheckbox(state = true, onClick = {})
            ThreeStateCheckbox(state = null, onClick = {})
            ThreeStateCheckbox(state = false, onClick = {})
        }
    }
}

@Preview(device = PIXEL_7_PRO)
@Composable
private fun EditCategoryCollectionScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    val categories = listOf(
        Category(
            id = 13,
            type = CategoryType.Expense,
            orderNum = 1,
            parentCategoryId = 1,
            name = "Groceries",
            icon = CategoryIcon.Other,
            color = CategoryColor.Olive
        ),
        Category(
            id = 14,
            type = CategoryType.Expense,
            orderNum = 2,
            parentCategoryId = null,
            name = "Category 2",
            icon = CategoryIcon.Other,
            color = CategoryColor.Olive
        ),
        Category(
            id = 25,
            type = CategoryType.Expense,
            orderNum = 3,
            parentCategoryId = null,
            name = "Category 3",
            icon = CategoryIcon.Other,
            color = CategoryColor.Olive
        ),
        Category(
            id = 30,
            type = CategoryType.Expense,
            orderNum = 3,
            parentCategoryId = null,
            name = "Category 3",
            icon = CategoryIcon.Other,
            color = CategoryColor.Olive
        )
    )
    val collection = CategoryCollectionWithCategories(
        id = 1,
        orderNum = 1,
        type = CategoryCollectionType.Expense,
        name = "Collection",
        categories = categories
    )

    PreviewContainer(appTheme = appTheme) {
        EditCategoryCollectionScreen(
            onNavigateBack = {},
            collection = collection,
            checkedGroupedCategoriesByType =
            DefaultCategoriesPackage(LocalContext.current).getDefaultCategories()
                .toCheckedCategoriesWithSubcategories(collection),
            expandedCategory = null,
            allowDeleting = true,
            allowSaving = true,
            onNameChange = {},
            onCheckedChange = {},
            onExpandedChange = {},
            onDeleteButton = {},
            onSaveButton = {}
        )
    }
}