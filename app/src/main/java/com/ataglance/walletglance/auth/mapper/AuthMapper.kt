package com.ataglance.walletglance.auth.mapper

import com.ataglance.walletglance.auth.domain.model.CurrentAppVersion
import com.ataglance.walletglance.auth.domain.model.user.User
import com.ataglance.walletglance.auth.domain.model.user.UserRole
import com.ataglance.walletglance.auth.domain.model.user.UserWithToken
import com.ataglance.walletglance.billing.domain.model.AppSubscription
import com.ataglance.walletglance.core.domain.app.AppLanguage
import com.glanci.auth.shared.dto.AppSubscriptionDto
import com.glanci.auth.shared.dto.CheckAppVersionRequestDto
import com.glanci.auth.shared.dto.UserDto
import com.glanci.auth.shared.dto.UserRoleDto
import com.glanci.auth.shared.dto.UserWithTokenDto


fun CurrentAppVersion.toDto(): CheckAppVersionRequestDto {
    return CheckAppVersionRequestDto(
        primaryVersion = primary,
        secondaryVersion = secondary,
        tertiaryVersion = tertiary,
        alphaVersion = alpha,
        betaVersion = beta,
        releaseCandidateVersion = rc
    )
}


fun UserRoleDto.toDomainModel(): UserRole {
    return when (this) {
        UserRoleDto.User -> UserRole.User
        UserRoleDto.Admin -> UserRole.Admin
    }
}

fun AppSubscriptionDto.toDomainModel(): AppSubscription {
    return when (this) {
        AppSubscriptionDto.Base -> AppSubscription.Base
        AppSubscriptionDto.Premium -> AppSubscription.Premium
    }
}


fun UserDto.toDomainModel(): User {
    return User(
        id = id,
        email = email,
        role = role.toDomainModel(),
        name = name,
        language = AppLanguage.fromLangCode(langCode = langCode) ?: AppLanguage.English,
        subscription = subscription.toDomainModel(),
        timestamp = timestamp
    )
}


fun UserWithTokenDto.toDomainModel(): UserWithToken {
    return UserWithToken(
        id = id,
        email = email,
        role = role.toDomainModel(),
        name = name,
        language = AppLanguage.fromLangCode(langCode = langCode) ?: AppLanguage.English,
        subscription = subscription.toDomainModel(),
        timestamp = timestamp,
        token = token
    )
}
