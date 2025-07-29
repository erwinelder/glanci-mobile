package com.ataglance.walletglance.account.domain.repository

import com.ataglance.walletglance.account.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    suspend fun upsertAccounts(accounts: List<Account>)

    suspend fun deleteAndUpsertAccounts(toDelete: List<Account>, toUpsert: List<Account>)

    suspend fun deleteAllAccountsLocally()

    suspend fun getAccount(id: Int): Account?

    fun getAllAccountsAsFlow(): Flow<List<Account>>

    suspend fun getAllAccounts(): List<Account>

}