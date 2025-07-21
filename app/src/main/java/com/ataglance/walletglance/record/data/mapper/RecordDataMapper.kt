package com.ataglance.walletglance.record.data.mapper

import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.data.local.model.RecordEntityWithItems
import com.ataglance.walletglance.record.data.local.model.RecordItemEntity
import com.ataglance.walletglance.record.data.model.RecordDataModel
import com.ataglance.walletglance.record.data.model.RecordWithItemsDataModel
import com.ataglance.walletglance.record.data.model.RecordItemDataModel
import com.glanci.record.shared.dto.RecordCommandDto
import com.glanci.record.shared.dto.RecordItemDto
import com.glanci.record.shared.dto.RecordQueryDto
import com.glanci.record.shared.dto.RecordWithItemsCommandDto
import com.glanci.record.shared.dto.RecordWithItemsQueryDto


fun RecordDataModel.toEntity(timestamp: Long, deleted: Boolean): RecordEntity {
    return RecordEntity(
        id = id,
        date = date,
        type = type,
        accountId = accountId,
        includeInBudgets = includeInBudgets,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun RecordItemDataModel.toEntity(): RecordItemEntity {
    return RecordItemEntity(
        id = id,
        recordId = recordId,
        totalAmount = totalAmount,
        quantity = quantity,
        categoryId = categoryId,
        subcategoryId = subcategoryId,
        note = note
    )
}

fun RecordWithItemsDataModel.toEntityWithItems(
    timestamp: Long,
    deleted: Boolean
): RecordEntityWithItems {
    return RecordEntityWithItems(
        record = record.toEntity(timestamp = timestamp, deleted = deleted),
        items = items.map { it.toEntity() }
    )
}


fun RecordEntity.toDataModel(): RecordDataModel {
    return RecordDataModel(
        id = id,
        date = date,
        type = type,
        accountId = accountId,
        includeInBudgets = includeInBudgets
    )
}

fun RecordItemEntity.toDataModel(): RecordItemDataModel {
    return RecordItemDataModel(
        id = id,
        recordId = recordId,
        totalAmount = totalAmount,
        quantity = quantity,
        categoryId = categoryId,
        subcategoryId = subcategoryId,
        note = note
    )
}

fun RecordEntityWithItems.toDataModelWithItems(): RecordWithItemsDataModel {
    return RecordWithItemsDataModel(
        record = record.toDataModel(),
        items = items.map { it.toDataModel() }
    )
}


fun RecordDataModel.toCommandDto(timestamp: Long, deleted: Boolean): RecordCommandDto {
    return RecordCommandDto(
        id = id,
        date = date,
        type = type,
        accountId = accountId,
        includeInBudgets = includeInBudgets,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun RecordItemDataModel.toDto(): RecordItemDto {
    return RecordItemDto(
        id = id,
        recordId = recordId,
        totalAmount = totalAmount,
        quantity = quantity,
        categoryId = categoryId,
        subcategoryId = subcategoryId,
        note = note
    )
}

fun RecordWithItemsDataModel.toCommandDtoWithItems(
    timestamp: Long,
    deleted: Boolean
): RecordWithItemsCommandDto {
    return RecordWithItemsCommandDto(
        record = record.toCommandDto(timestamp = timestamp, deleted = deleted),
        items = items.map { it.toDto() }
    )
}


fun RecordEntity.toCommandDto(): RecordCommandDto {
    return RecordCommandDto(
        id = id,
        date = date,
        type = type,
        accountId = accountId,
        includeInBudgets = includeInBudgets,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun RecordItemEntity.toDto(): RecordItemDto {
    return RecordItemDto(
        id = id,
        recordId = recordId,
        totalAmount = totalAmount,
        quantity = quantity,
        categoryId = categoryId,
        subcategoryId = subcategoryId,
        note = note
    )
}

fun RecordEntityWithItems.toCommandDtoWithItems(): RecordWithItemsCommandDto {
    return RecordWithItemsCommandDto(
        record = record.toCommandDto(),
        items = items.map { it.toDto() }
    )
}


fun RecordQueryDto.toEntity(): RecordEntity {
    return RecordEntity(
        id = id,
        date = date,
        type = type,
        accountId = accountId,
        includeInBudgets = includeInBudgets,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun RecordItemDto.toEntity(): RecordItemEntity {
    return RecordItemEntity(
        id = id,
        recordId = recordId,
        totalAmount = totalAmount,
        quantity = quantity,
        categoryId = categoryId,
        subcategoryId = subcategoryId,
        note = note
    )
}

fun RecordWithItemsQueryDto.toEntityWithItems(): RecordEntityWithItems {
    return RecordEntityWithItems(
        record = record.toEntity(),
        items = items.map { it.toEntity() }
    )
}
