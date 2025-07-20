package com.ataglance.walletglance.budget.data.model

data class BudgetWithAssociationsDataModel(
    val budget: BudgetDataModel,
    val associations: List<BudgetAccountAssociationDataModel>
) {

    val budgetId: Int
        get() = budget.id

}
