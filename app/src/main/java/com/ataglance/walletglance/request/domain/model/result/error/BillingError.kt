package com.ataglance.walletglance.request.domain.model.result.error

import com.ataglance.walletglance.request.domain.model.result.error.DomainError

enum class BillingError : DomainError {
    UserCancelledPurchase,
    UserNotSignedIn,
    NoNetwork,
    Unknown
}