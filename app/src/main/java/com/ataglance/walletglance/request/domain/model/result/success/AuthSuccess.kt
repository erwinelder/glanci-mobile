package com.ataglance.walletglance.request.domain.model.result.success

import com.ataglance.walletglance.request.domain.model.result.DomainSuccess

enum class AuthSuccess : DomainSuccess {
    SignedIn,
    SignUpEmailVerificationSent,
    SignUpVerificationCodeReceived,
    SignedUp,
    EmailUpdateEmailVerificationSent,
    EmailUpdateVerificationCodeReceived,
    EmailUpdated,
    ResetPasswordEmailSent,
    PasswordUpdated,
    NameUpdated,
    AccountDeleted
}