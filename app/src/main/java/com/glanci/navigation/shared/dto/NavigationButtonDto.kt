package com.glanci.navigation.shared.dto

import kotlinx.serialization.Serializable

@Serializable
data class NavigationButtonDto(
    val screenName: String,
    val orderNum: Int,
    val timestamp: Long,
    val deleted: Boolean
)