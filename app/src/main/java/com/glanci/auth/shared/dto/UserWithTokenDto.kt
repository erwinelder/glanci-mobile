package com.glanci.auth.shared.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserWithTokenDto(
    val id: Int,
    val email: String,
    val role: UserRoleDto,
    val name: String,
    val langCode: String,
    val subscription: AppSubscriptionDto,
    val timestamp: Long,
    val token: String
)