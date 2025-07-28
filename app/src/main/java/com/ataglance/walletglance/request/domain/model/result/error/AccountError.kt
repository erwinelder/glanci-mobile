package com.ataglance.walletglance.request.domain.model.result.error

enum class AccountError : DomainError {
    AccountsNotSaved,
    AccountsNotFetched,
    AccountsSynchronizationFailed
}