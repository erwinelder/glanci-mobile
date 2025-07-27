package com.ataglance.walletglance.billing.domain.model.errorHandling

import com.ataglance.walletglance.request.domain.model.result.DomainError

enum class BillingError : DomainError {
    UserCancelledPurchase,
    UserNotSignedIn,
    NoNetwork,
    Unknown
}