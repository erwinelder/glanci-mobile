package com.ataglance.walletglance.account.domain.model

data class AccountsAndActiveOne(
    val accounts: List<Account> = emptyList(),
    val activeAccount: Account? = null
) {

    companion object {

        fun fromAccounts(
            accounts: List<Account>,
            currentActiveAccountId: Int?
        ): AccountsAndActiveOne {
            val accounts = if (currentActiveAccountId == null) {
                accounts.toMutableList().apply {
                    this[0] = this[0].copy(isActive = true)
                }
            } else {
                accounts.map { it.copy(isActive = it.id == currentActiveAccountId) }
            }

            return AccountsAndActiveOne(
                accounts = accounts,
                activeAccount = accounts.firstOrNull { it.isActive }
            )
        }

    }

}