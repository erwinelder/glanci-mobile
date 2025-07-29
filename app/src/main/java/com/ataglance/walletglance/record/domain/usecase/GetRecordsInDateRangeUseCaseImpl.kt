package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.record.domain.repository.RecordRepository
import com.ataglance.walletglance.transaction.domain.model.RecordWithItems
import kotlinx.coroutines.flow.Flow

class GetRecordsInDateRangeUseCaseImpl(
    private val recordRepository: RecordRepository
) : GetRecordsInDateRangeUseCase {

    override fun getAsFlow(range: TimestampRange): Flow<List<RecordWithItems>> {
        return recordRepository.getRecordsWithItemsInDateRangeAsFlow(
            from = range.from, to = range.to
        )
    }

    override suspend fun get(range: TimestampRange): List<RecordWithItems> {
        return recordRepository.getRecordsWithItemsInDateRange(from = range.from, to = range.to)
    }

}