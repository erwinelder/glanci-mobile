package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.account.domain.utils.findById
import com.ataglance.walletglance.category.domain.model.CategoryWithSub
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.domain.usecase.GetCategoriesGroupedUseCase
import com.ataglance.walletglance.record.domain.repository.RecordRepository
import com.ataglance.walletglance.record.mapper.toDraftWithItems
import com.ataglance.walletglance.record.presentation.model.RecordDraftWithItems

class GetRecordDraftUseCaseImpl(
    private val recordRepository: RecordRepository,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getCategoriesGroupedUseCase: GetCategoriesGroupedUseCase
) : GetRecordDraftUseCase {

    override suspend fun execute(
        id: Long?,
        accountId: Int?,
        accounts: List<Account>?,
        categoryWithSub: CategoryWithSub?,
        categories: GroupedCategoriesByType?
    ): RecordDraftWithItems {
        val accounts = accounts ?: getAccountsUseCase.get()
        val categories = categories ?: getCategoriesGroupedUseCase.get()

        return id
            ?.let { recordRepository.getRecordWithItems(id = id) }
            ?.toDraftWithItems(accounts = accounts, categories = categories)
            ?: RecordDraftWithItems.asNew(
                account = accountId?.let { accounts.findById(accountId) },
                categoryWithSub = categoryWithSub
            )
    }

}