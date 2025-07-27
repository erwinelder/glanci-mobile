package com.ataglance.walletglance.auth.mapper

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ataglance.walletglance.R
import com.ataglance.walletglance.request.data.model.result.error.AuthDataError
import com.ataglance.walletglance.request.domain.model.result.error.AuthError
import com.ataglance.walletglance.request.domain.model.result.success.AuthSuccess
import com.ataglance.walletglance.request.presentation.model.ResultState


fun AuthDataError.toDomainError(): AuthError {
    return when (this) {
        AuthDataError.InvalidToken -> AuthError.SessionExpired
        AuthDataError.InsufficientPermissions -> AuthError.InsufficientPermissions
        AuthDataError.AppVersionIsBelowRequired -> AuthError.AppUpdateRequired
        AuthDataError.ErrorDuringExtractingJwtSecret,
        AuthDataError.ErrorDuringCreatingJwtToken -> AuthError.AuthSessionCreationError
        AuthDataError.SignUpFailed -> AuthError.SignUpFailed

        AuthDataError.OobCodeExpired -> AuthError.OobCodeExpired
        AuthDataError.InvalidOobCode -> AuthError.InvalidOobCode
        AuthDataError.ErrorDuringFetchingUserDataFromAuthProvider -> AuthError.ErrorDuringFetchingUserDataFromAuthProvider
        AuthDataError.ErrorDuringVerifyingOobCodeAtAuthProvider -> AuthError.ErrorDuringVerifyingOobCodeAtAuthProvider
        AuthDataError.ErrorDuringDeletingUserAtAuthProvider -> AuthError.ErrorDuringDeletingUserAtAuthProvider

        AuthDataError.SendingVerificationEmailFailed -> AuthError.SendingVerificationEmailFailed
        AuthDataError.EmailVerificationFailed -> AuthError.EmailVerificationFailed
        AuthDataError.EmailNotVerified -> AuthError.EmailNotVerified

        AuthDataError.EmailUpdateRequestFailed -> AuthError.EmailUpdateRequestFailed
        AuthDataError.EmailUpdateFailed -> AuthError.EmailUpdateFailed

        AuthDataError.PasswordUpdateFailed -> AuthError.PasswordUpdateFailed

        AuthDataError.PasswordResetRequestFailed -> AuthError.PasswordResetRequestFailed
        AuthDataError.PasswordResetFailed -> AuthError.PasswordResetFailed

        AuthDataError.InvalidCredentials -> AuthError.InvalidCredentials
        AuthDataError.InvalidEmail -> AuthError.InvalidEmail
        AuthDataError.InvalidPassword -> AuthError.InvalidPassword
        AuthDataError.InvalidName -> AuthError.InvalidName
        AuthDataError.InvalidLanguage -> AuthError.InvalidLanguage

        AuthDataError.UserAlreadyExists -> AuthError.UserAlreadyExists
        AuthDataError.UserNotFound -> AuthError.UserNotFound
        AuthDataError.UserNotFetched -> AuthError.UserNotFetched
        AuthDataError.UserNotCreated -> AuthError.UserNotCreated
        AuthDataError.UserEmailNotSaved -> AuthError.UserEmailNotSaved
        AuthDataError.UserNameNotSaved -> AuthError.UserNameNotSaved
        AuthDataError.UserLanguageNotSaved -> AuthError.UserLanguageNotSaved
        AuthDataError.UserSubscriptionNotSaved -> AuthError.UserSubscriptionNotSaved
        AuthDataError.UserNotDeleted -> AuthError.UserDeletionFailed
    }
}


fun AuthSuccess.toResultStateButton(): ResultState.ButtonState {
    return ResultState.ButtonState(
        titleRes = this.asTitleRes(),
        messageRes = this.asMessageResOrNull(),
        buttonTextRes = this.asButtonTextRes(),
        buttonIconRes = this.asButtonIconResOrNull()
    )
}


fun AuthError.toResultStateButton(): ResultState.ButtonState {
    return ResultState.ButtonState(
        titleRes = this.asTitleRes(),
        messageRes = this.asMessageResOrNull(),
        buttonTextRes = this.asButtonTextRes(),
        buttonIconRes = this.asButtonIconResOrNull()
    )
}


@StringRes private fun AuthSuccess.asTitleRes(): Int {
    return when (this) {
        AuthSuccess.SignedIn -> R.string.welcome_back_to_glance
        AuthSuccess.SignUpEmailVerificationSent -> R.string.email_sent
        AuthSuccess.SignUpVerificationCodeReceived -> R.string.verify_email
        AuthSuccess.SignedUp -> R.string.welcome_to_glance
        AuthSuccess.EmailUpdateEmailVerificationSent -> R.string.email_sent
        AuthSuccess.EmailUpdateVerificationCodeReceived -> R.string.verify_email
        AuthSuccess.EmailUpdated -> R.string.all_set
        AuthSuccess.ResetPasswordEmailSent -> R.string.email_sent
        AuthSuccess.PasswordUpdated -> R.string.all_set
        AuthSuccess.NameUpdated -> R.string.all_set
        AuthSuccess.AccountDeleted -> R.string.account_deleted
    }
}

@StringRes private fun AuthSuccess.asMessageResOrNull(): Int? {
    return when (this) {
        AuthSuccess.SignedIn -> null
        AuthSuccess.SignUpEmailVerificationSent -> R.string.sign_up_email_verification_sent_message
        AuthSuccess.SignUpVerificationCodeReceived -> R.string.sign_up_verify_email_description
        AuthSuccess.SignedUp -> null
        AuthSuccess.EmailUpdateEmailVerificationSent -> R.string.update_email_email_verification_sent_message
        AuthSuccess.EmailUpdateVerificationCodeReceived -> R.string.email_update_verify_email_description
        AuthSuccess.EmailUpdated -> R.string.email_updated_successfully_message
        AuthSuccess.ResetPasswordEmailSent -> R.string.reset_password_email_sent_message
        AuthSuccess.PasswordUpdated -> R.string.password_updated_successfully_message
        AuthSuccess.NameUpdated -> R.string.name_updated_successfully_message
        AuthSuccess.AccountDeleted -> R.string.your_account_deleted_successfully_message
    }
}

@StringRes private fun AuthSuccess.asButtonTextRes(): Int {
    return when (this) {
        AuthSuccess.SignedIn -> R.string.continue_to_app
        AuthSuccess.SignUpEmailVerificationSent -> R.string.check
        AuthSuccess.SignUpVerificationCodeReceived -> R.string.verify
        AuthSuccess.SignedUp -> R.string.continue_setup
        AuthSuccess.EmailUpdateEmailVerificationSent -> R.string.check
        AuthSuccess.EmailUpdateVerificationCodeReceived -> R.string.verify
        AuthSuccess.EmailUpdated -> R.string._continue
        AuthSuccess.ResetPasswordEmailSent -> R.string.back
        AuthSuccess.PasswordUpdated -> R.string._continue
        AuthSuccess.NameUpdated -> R.string._continue
        AuthSuccess.AccountDeleted -> R.string._continue
    }
}

@DrawableRes private fun AuthSuccess.asButtonIconResOrNull(): Int? {
    return when (this) {
        AuthSuccess.SignedIn -> null
        AuthSuccess.SignUpEmailVerificationSent -> null
        AuthSuccess.SignUpVerificationCodeReceived -> null
        AuthSuccess.SignedUp -> null
        AuthSuccess.EmailUpdateEmailVerificationSent -> null
        AuthSuccess.EmailUpdateVerificationCodeReceived -> null
        AuthSuccess.EmailUpdated -> null
        AuthSuccess.ResetPasswordEmailSent -> R.drawable.short_arrow_left_icon
        AuthSuccess.PasswordUpdated -> null
        AuthSuccess.NameUpdated -> null
        AuthSuccess.AccountDeleted -> null
    }
}


@StringRes private fun AuthError.asTitleRes(): Int {
    return when (this) {
        AuthError.SessionExpired -> R.string.session_expired_title
        AuthError.InsufficientPermissions -> R.string.insufficient_permissions_title
        AuthError.AppUpdateRequired -> R.string.app_update_required_title
        AuthError.AuthSessionCreationError -> R.string.oops
        AuthError.SignUpFailed -> R.string.oops

        AuthError.OobCodeExpired -> R.string.oops
        AuthError.InvalidOobCode -> R.string.oops
        AuthError.ErrorDuringFetchingUserDataFromAuthProvider -> R.string.oops
        AuthError.ErrorDuringVerifyingOobCodeAtAuthProvider -> R.string.oops
        AuthError.ErrorDuringDeletingUserAtAuthProvider -> R.string.oops

        AuthError.SendingVerificationEmailFailed -> R.string.oops
        AuthError.EmailVerificationFailed -> R.string.oops
        AuthError.EmailNotVerified -> R.string.email_not_verified_title

        AuthError.EmailUpdateRequestFailed -> R.string.oops
        AuthError.EmailUpdateFailed -> R.string.oops

        AuthError.PasswordUpdateFailed -> R.string.oops

        AuthError.PasswordResetRequestFailed -> R.string.oops
        AuthError.PasswordResetFailed -> R.string.oops

        AuthError.InvalidCredentials -> R.string.oops
        AuthError.InvalidEmail -> R.string.oops
        AuthError.InvalidPassword -> R.string.oops
        AuthError.InvalidName -> R.string.oops
        AuthError.InvalidLanguage -> R.string.oops

        AuthError.UserAlreadyExists -> R.string.oops
        AuthError.UserNotFound -> R.string.oops
        AuthError.UserNotFetched -> R.string.oops
        AuthError.UserNotCreated -> R.string.oops
        AuthError.UserEmailNotSaved -> R.string.oops
        AuthError.UserNameNotSaved -> R.string.oops
        AuthError.UserLanguageNotSaved -> R.string.oops
        AuthError.UserSubscriptionNotSaved -> R.string.oops
        AuthError.UserDeletionFailed -> R.string.oops
    }
}

@StringRes private fun AuthError.asMessageResOrNull(): Int? {
    return when (this) {
        AuthError.SessionExpired -> R.string.session_expired_message
        AuthError.InsufficientPermissions -> R.string.insufficient_permissions_message
        AuthError.AppUpdateRequired -> R.string.app_update_required_message
        AuthError.AuthSessionCreationError -> R.string.auth_session_creation_error_message
        AuthError.SignUpFailed -> R.string.sign_up_failed_message

        AuthError.OobCodeExpired -> R.string.verification_code_expired_message
        AuthError.InvalidOobCode -> R.string.verification_code_invalid_message
        AuthError.ErrorDuringFetchingUserDataFromAuthProvider -> R.string.fetching_user_data_from_auth_provider_error_message
        AuthError.ErrorDuringVerifyingOobCodeAtAuthProvider -> R.string.verifying_verification_code_at_auth_provider_error_message
        AuthError.ErrorDuringDeletingUserAtAuthProvider -> R.string.deleting_user_at_auth_provider_error_message

        AuthError.SendingVerificationEmailFailed -> R.string.sending_email_verification_email_failed_message
        AuthError.EmailVerificationFailed -> R.string.email_verification_failed_message
        AuthError.EmailNotVerified -> R.string.email_not_verified_message

        AuthError.EmailUpdateRequestFailed -> R.string.email_update_request_failed_message
        AuthError.EmailUpdateFailed -> R.string.email_update_failed_message

        AuthError.PasswordUpdateFailed -> R.string.password_update_failed_message

        AuthError.PasswordResetRequestFailed -> R.string.password_reset_request_failed_message
        AuthError.PasswordResetFailed -> R.string.password_reset_failed_message

        AuthError.InvalidCredentials -> R.string.invalid_credentials_message
        AuthError.InvalidEmail -> R.string.invalid_email_message
        AuthError.InvalidPassword -> R.string.invalid_password_message
        AuthError.InvalidName -> R.string.invalid_name_message
        AuthError.InvalidLanguage -> R.string.invalid_language_message

        AuthError.UserAlreadyExists -> R.string.user_already_exists_message
        AuthError.UserNotFound -> R.string.user_not_found_message
        AuthError.UserNotFetched -> R.string.user_not_fetched_message
        AuthError.UserNotCreated -> R.string.user_not_created_message
        AuthError.UserEmailNotSaved -> R.string.user_email_not_saved_message
        AuthError.UserNameNotSaved -> R.string.user_name_not_saved_message
        AuthError.UserLanguageNotSaved -> R.string.user_language_not_saved_message
        AuthError.UserSubscriptionNotSaved -> R.string.user_subscription_not_saved_message
        AuthError.UserDeletionFailed -> R.string.account_deletion_failed_message
    }
}

@StringRes private fun AuthError.asButtonTextRes(): Int {
    return when (this) {
        AuthError.SessionExpired -> R.string.sign_in
        AuthError.InsufficientPermissions -> R.string.close
        AuthError.AppUpdateRequired -> R.string.update
        AuthError.AuthSessionCreationError -> R.string.close
        AuthError.SignUpFailed -> R.string.close

        AuthError.OobCodeExpired -> R.string.close
        AuthError.InvalidOobCode -> R.string.close
        AuthError.ErrorDuringFetchingUserDataFromAuthProvider -> R.string.close
        AuthError.ErrorDuringVerifyingOobCodeAtAuthProvider -> R.string.close
        AuthError.ErrorDuringDeletingUserAtAuthProvider -> R.string.close

        AuthError.SendingVerificationEmailFailed -> R.string.close
        AuthError.EmailVerificationFailed -> R.string.close
        AuthError.EmailNotVerified -> R.string.close

        AuthError.EmailUpdateRequestFailed -> R.string.close
        AuthError.EmailUpdateFailed -> R.string.close

        AuthError.PasswordUpdateFailed -> R.string.close

        AuthError.PasswordResetRequestFailed -> R.string.close
        AuthError.PasswordResetFailed -> R.string.close

        AuthError.InvalidCredentials -> R.string.close
        AuthError.InvalidEmail -> R.string.close
        AuthError.InvalidPassword -> R.string.close
        AuthError.InvalidName -> R.string.close
        AuthError.InvalidLanguage -> R.string.close

        AuthError.UserAlreadyExists -> R.string.close
        AuthError.UserNotFound -> R.string.close
        AuthError.UserNotFetched -> R.string.close
        AuthError.UserNotCreated -> R.string.close
        AuthError.UserEmailNotSaved -> R.string.close
        AuthError.UserNameNotSaved -> R.string.close
        AuthError.UserLanguageNotSaved -> R.string.close
        AuthError.UserSubscriptionNotSaved -> R.string.close
        AuthError.UserDeletionFailed -> R.string.close
    }
}

@DrawableRes private fun AuthError.asButtonIconResOrNull(): Int? {
    return when (this) {
        AuthError.SessionExpired -> null
        AuthError.InsufficientPermissions -> R.drawable.close_icon
        AuthError.AppUpdateRequired -> null
        AuthError.AuthSessionCreationError -> R.drawable.close_icon
        AuthError.SignUpFailed -> R.drawable.close_icon

        AuthError.OobCodeExpired -> R.drawable.close_icon
        AuthError.InvalidOobCode -> R.drawable.close_icon
        AuthError.ErrorDuringFetchingUserDataFromAuthProvider -> R.drawable.close_icon
        AuthError.ErrorDuringVerifyingOobCodeAtAuthProvider -> R.drawable.close_icon
        AuthError.ErrorDuringDeletingUserAtAuthProvider -> R.drawable.close_icon

        AuthError.SendingVerificationEmailFailed -> R.drawable.close_icon
        AuthError.EmailVerificationFailed -> R.drawable.close_icon
        AuthError.EmailNotVerified -> R.drawable.close_icon

        AuthError.EmailUpdateRequestFailed -> R.drawable.close_icon
        AuthError.EmailUpdateFailed -> R.drawable.close_icon

        AuthError.PasswordUpdateFailed -> R.drawable.close_icon

        AuthError.PasswordResetRequestFailed -> R.drawable.close_icon
        AuthError.PasswordResetFailed -> R.drawable.close_icon

        AuthError.InvalidCredentials -> R.drawable.close_icon
        AuthError.InvalidEmail -> R.drawable.close_icon
        AuthError.InvalidPassword -> R.drawable.close_icon
        AuthError.InvalidName -> R.drawable.close_icon
        AuthError.InvalidLanguage -> R.drawable.close_icon

        AuthError.UserAlreadyExists -> R.drawable.close_icon
        AuthError.UserNotFound -> R.drawable.close_icon
        AuthError.UserNotFetched -> R.drawable.close_icon
        AuthError.UserNotCreated -> R.drawable.close_icon
        AuthError.UserEmailNotSaved -> R.drawable.close_icon
        AuthError.UserNameNotSaved -> R.drawable.close_icon
        AuthError.UserLanguageNotSaved -> R.drawable.close_icon
        AuthError.UserSubscriptionNotSaved -> R.drawable.close_icon
        AuthError.UserDeletionFailed -> R.drawable.close_icon
    }
}
