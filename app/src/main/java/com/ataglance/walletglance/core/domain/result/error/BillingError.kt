package com.ataglance.walletglance.core.domain.result.error

enum class BillingError : DomainError {
    UserCancelledPurchase,
    UserNotSignedIn,
    NoNetwork,
    Unknown
}