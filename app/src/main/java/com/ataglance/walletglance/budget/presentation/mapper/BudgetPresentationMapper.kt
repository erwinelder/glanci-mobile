package com.ataglance.walletglance.budget.presentation.mapper

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.budget.presentation.model.BudgetDraft
import com.ataglance.walletglance.budget.presentation.model.BudgetUiState


fun BudgetUiState.toDraft(accounts: List<Account>): BudgetDraft {
    return BudgetDraft(
        isNew = false,
        id = id,
        amountLimit = amountLimit,
        category = category,
        name = name,
        currRepeatingPeriod = repeatingPeriod,
        newRepeatingPeriod = repeatingPeriod,
        linkedAccounts = accounts.filter { it.id in accountIds }
    )
}


fun BudgetDraft.toUiState(): BudgetUiState? {
    return BudgetUiState(
        id = id,
        amountLimit = amountLimit,
        category = category ?: return null,
        priorityNum = priorityNum,
        name = name,
        repeatingPeriod = newRepeatingPeriod,
        currency = linkedAccounts.firstOrNull()?.currency ?: "",
        accountIds = linkedAccounts.map { it.id }
    )
}
