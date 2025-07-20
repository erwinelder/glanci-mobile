package com.glanci.account.shared.dto

import kotlinx.serialization.Serializable

@Serializable
data class AccountCommandDto(
    val id: Int,
    val orderNum: Int,
    val name: String,
    val currency: String,
    val balance: Double,
    val color: String,
    val hide: Boolean,
    val hideBalance: Boolean,
    val withoutBalance: Boolean,
    val timestamp: Long,
    val deleted: Boolean
)