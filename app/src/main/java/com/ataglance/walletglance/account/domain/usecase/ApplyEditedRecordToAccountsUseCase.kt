package com.ataglance.walletglance.account.domain.usecase

import com.ataglance.walletglance.transaction.domain.model.RecordWithItems

interface ApplyEditedRecordToAccountsUseCase {

    suspend fun execute(createdRecord: RecordWithItems, currentRecord: RecordWithItems): Boolean

}