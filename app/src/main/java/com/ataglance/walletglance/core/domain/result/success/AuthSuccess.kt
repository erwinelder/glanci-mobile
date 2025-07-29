package com.ataglance.walletglance.core.domain.result.success

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