package com.glanci.record.shared.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecordWithItemsCommandDto(
    val record: RecordCommandDto,
    val items: List<RecordItemDto>
) {

    val recordId: Long
        get() = record.id

    val deleted: Boolean
        get() = record.deleted

}
