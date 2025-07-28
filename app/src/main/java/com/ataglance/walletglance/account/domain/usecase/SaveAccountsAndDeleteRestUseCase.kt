package com.ataglance.walletglance.account.domain.usecase

import com.ataglance.walletglance.account.domain.model.Account

interface SaveAccountsAndDeleteRestUseCase {

    suspend fun execute(accounts: List<Account>)

}