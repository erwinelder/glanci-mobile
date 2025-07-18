package com.ataglance.walletglance.record.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RecordQueryDto(
    val userId: Int,
    val id: Long,
    val date: Long,
    val type: Char,
    val accountId: Int,
    val includeInBudgets: Boolean,
    val timestamp: Long,
    val deleted: Boolean
)
