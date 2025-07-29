package com.ataglance.walletglance.core.domain.result.error

enum class AuthError : DomainError {
    SessionExpired,
    InsufficientPermissions,
    AppUpdateRequired,
    AuthSessionCreationError,
    SignUpFailed,

    OobCodeExpired,
    InvalidOobCode,
    ErrorDuringFetchingUserDataFromAuthProvider,
    ErrorDuringVerifyingOobCodeAtAuthProvider,
    ErrorDuringDeletingUserAtAuthProvider,

    SendingVerificationEmailFailed,
    EmailVerificationFailed,
    EmailNotVerified,

    EmailUpdateRequestFailed,
    EmailUpdateFailed,

    PasswordUpdateFailed,

    PasswordResetRequestFailed,
    PasswordResetFailed,

    InvalidCredentials,
    InvalidEmail,
    InvalidPassword,
    InvalidName,
    InvalidLanguage,

    UserAlreadyExists,
    UserNotFound,
    UserNotFetched,
    UserNotCreated,
    UserEmailNotSaved,
    UserNameNotSaved,
    UserLanguageNotSaved,
    UserSubscriptionNotSaved,
    UserDeletionFailed
}