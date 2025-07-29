package com.ataglance.walletglance.account.domain.usecase

import com.ataglance.walletglance.transaction.domain.model.RecordWithItems

interface RollbackRecordToAccountUseCase {

    suspend fun execute(recordWithItems: RecordWithItems): Boolean

}