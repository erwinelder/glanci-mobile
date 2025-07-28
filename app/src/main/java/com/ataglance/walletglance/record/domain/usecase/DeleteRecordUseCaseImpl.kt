package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.account.domain.repository.AccountRepository
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.core.utils.asList
import com.ataglance.walletglance.record.data.repository.RecordRepository

class DeleteRecordUseCaseImpl(
    private val recordRepository: RecordRepository,
    private val accountRepository: AccountRepository
) : DeleteRecordUseCase {
    
    override suspend fun execute(id: Long) {
        val record = recordRepository.getRecordWithItems(id = id) ?: return
        val type = CategoryType.fromChar(char = record.type) ?: return

        val account = accountRepository.getAccount(id = record.accountId)
            ?.rollbackTransaction(amount = record.totalAmount, type = type)
            ?: return

        recordRepository.deleteRecordWithItems(recordWithItems = record)
        accountRepository.upsertAccounts(accounts = account.asList())
    }

}