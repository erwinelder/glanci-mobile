package com.ataglance.walletglance.request.domain.model.result.success

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