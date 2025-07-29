package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.repository.AccountRepository
import com.ataglance.walletglance.budget.data.repository.BudgetRepository
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.mapper.budget.toDomainModel
import com.ataglance.walletglance.category.domain.usecase.GetExpenseCategoriesGroupedUseCase

class GetEmptyBudgetsUseCaseImpl(
    private val budgetRepository: BudgetRepository,
    private val getExpenseCategoriesGroupedUseCase: GetExpenseCategoriesGroupedUseCase,
    private val accountRepository: AccountRepository
) : GetEmptyBudgetsUseCase {

    override suspend fun get(id: Int): Budget? {
        val accounts = accountRepository.getAllAccounts()
        return get(id = id, accounts = accounts)
    }

    override suspend fun get(id: Int, accounts: List<Account>): Budget? {
        val budgetWithAssociations = budgetRepository.getBudgetWithAssociations(budgetId = id)
            ?: return null
        val groupedCategoriesList = getExpenseCategoriesGroupedUseCase.execute()

        return budgetWithAssociations.toDomainModel(
            groupedCategoriesList = groupedCategoriesList,
            accounts = accounts
        )
    }

    override suspend fun get(): List<Budget> {
        val groupedCategoriesList = getExpenseCategoriesGroupedUseCase.execute()
        val accounts = accountRepository.getAllAccounts()

        return budgetRepository.getAllBudgetsWithAssociations().mapNotNull { budget ->
            budget.toDomainModel(groupedCategoriesList = groupedCategoriesList, accounts = accounts)
        }
    }

}