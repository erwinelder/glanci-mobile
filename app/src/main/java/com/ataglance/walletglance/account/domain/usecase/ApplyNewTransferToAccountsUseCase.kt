package com.ataglance.walletglance.account.domain.usecase

import com.ataglance.walletglance.transaction.domain.model.Transfer

interface ApplyNewTransferToAccountsUseCase {

    suspend fun execute(transfer: Transfer): Boolean

}