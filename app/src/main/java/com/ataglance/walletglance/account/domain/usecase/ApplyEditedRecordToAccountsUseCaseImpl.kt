package com.ataglance.walletglance.account.domain.usecase

import com.ataglance.walletglance.account.domain.repository.AccountRepository
import com.ataglance.walletglance.core.utils.asList
import com.ataglance.walletglance.transaction.domain.model.RecordWithItems

class ApplyEditedRecordToAccountsUseCaseImpl(
    private val accountRepository: AccountRepository
) : ApplyEditedRecordToAccountsUseCase {

    override suspend fun execute(createdRecord: RecordWithItems, currentRecord: RecordWithItems): Boolean {
        val accounts = if (createdRecord.accountId == currentRecord.accountId) {
            accountRepository
                .getAccount(id = createdRecord.accountId)
                ?.reapplyTransaction(
                    prevAmount = currentRecord.totalAmount,
                    newAmount = createdRecord.totalAmount,
                    type = createdRecord.type
                )
                ?.asList()
                ?: return false
        } else {
            val prevAccount = accountRepository
                .getAccount(id = currentRecord.accountId)
                ?.rollbackTransaction(amount = currentRecord.totalAmount, type = currentRecord.type)
                ?: return false
            val newAccount = accountRepository
                .getAccount(id = createdRecord.accountId)
                ?.applyTransaction(amount = createdRecord.totalAmount, type = createdRecord.type)
                ?: return false
            listOf(prevAccount, newAccount)
        }

        accountRepository.upsertAccounts(accounts = accounts)

        return true
    }

}