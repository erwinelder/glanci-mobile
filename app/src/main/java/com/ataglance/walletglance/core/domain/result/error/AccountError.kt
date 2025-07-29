package com.ataglance.walletglance.core.domain.result.error

enum class AccountError : DomainError {
    AccountsNotSaved,
    AccountsNotFetched,
    AccountsSynchronizationFailed
}