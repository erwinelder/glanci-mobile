package com.ataglance.walletglance.auth.domain.model.user

import com.ataglance.walletglance.billing.domain.model.AppSubscription
import com.ataglance.walletglance.core.domain.app.AppLanguage

data class UserWithToken(
    val id: Int,
    val email: String,
    val role: UserRole,
    val name: String,
    val language: AppLanguage,
    val subscription: AppSubscription,
    val timestamp: Long,
    val token: String
)