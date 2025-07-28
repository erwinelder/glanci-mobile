package com.glanci.request.shared.error

enum class AuthDataError : DataError {
    InvalidToken,
    InsufficientPermissions,
    AppVersionIsBelowRequired,
    ErrorDuringExtractingJwtSecret,
    ErrorDuringCreatingJwtToken,
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
    UserNotDeleted
}