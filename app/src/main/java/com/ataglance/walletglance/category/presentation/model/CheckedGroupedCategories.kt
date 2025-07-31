package com.ataglance.walletglance.category.presentation.model

import com.ataglance.walletglance.category.domain.model.Category

data class CheckedGroupedCategories(
    val category: Category,
    val checked: Boolean?,
    val subcategories: List<CheckedCategory>,
    val expanded: Boolean = false
) {

    fun inverseCheckedState(): CheckedGroupedCategories {
        val newChecked = !(checked ?: false)

        return this.copy(
            checked = newChecked,
            subcategories = subcategories.map { it.copy(checked = newChecked) }
        )
    }

}