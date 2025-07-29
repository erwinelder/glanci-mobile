package com.ataglance.walletglance.account.domain.usecase

import com.ataglance.walletglance.transaction.domain.model.Transfer

interface RollbackTransferToAccountsUseCase {

    suspend fun execute(transfer: Transfer): Boolean

}