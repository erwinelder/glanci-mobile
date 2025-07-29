package com.ataglance.walletglance.account.domain.usecase

import com.ataglance.walletglance.transaction.domain.model.Transfer

interface ApplyEditedTransferToAccountsUseCase {

    suspend fun execute(newTransfer: Transfer, currentTransfer: Transfer): Boolean

}