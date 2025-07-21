package com.glanci.record.shared.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecordItemDto(
    val id: Long,
    val recordId: Long,
    val totalAmount: Double,
    val quantity: Int?,
    val categoryId: Int,
    val subcategoryId: Int?,
    val note: String?
)
