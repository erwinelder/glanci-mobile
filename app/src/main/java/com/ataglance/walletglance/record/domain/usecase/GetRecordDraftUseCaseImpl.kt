package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.account.domain.repository.AccountRepository
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.utils.findById
import com.ataglance.walletglance.category.domain.model.CategoryWithSub
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.domain.usecase.GetCategoriesUseCase
import com.ataglance.walletglance.record.mapper.toDraftWithItems
import com.ataglance.walletglance.record.presentation.model.RecordDraftWithItems

class GetRecordDraftUseCaseImpl(
    private val getRecordUseCase: GetRecordUseCase,
    private val accountRepository: AccountRepository,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : GetRecordDraftUseCase {

    override suspend fun execute(
        id: Long?,
        accountId: Int?,
        accounts: List<Account>?,
        categoryWithSub: CategoryWithSub?,
        categories: GroupedCategoriesByType?
    ): RecordDraftWithItems {
        val accounts = accounts ?: accountRepository.getAllAccounts()
        val categories = categories ?: getCategoriesUseCase.getGrouped()

        return id
            ?.let { getRecordUseCase.get(id = id) }
            ?.toDraftWithItems(accounts = accounts, categories = categories)
            ?: RecordDraftWithItems.asNew(
                account = accountId?.let { accounts.findById(accountId) },
                categoryWithSub = categoryWithSub
            )
    }

}