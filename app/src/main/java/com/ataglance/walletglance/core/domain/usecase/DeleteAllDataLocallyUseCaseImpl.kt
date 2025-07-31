package com.ataglance.walletglance.core.domain.usecase

import com.ataglance.walletglance.account.domain.repository.AccountRepository
import com.ataglance.walletglance.category.domain.repository.CategoryRepository
import com.ataglance.walletglance.categoryCollection.domain.repository.CategoryCollectionRepository
import com.ataglance.walletglance.core.domain.repository.LocalUpdateTimeRepository
import com.ataglance.walletglance.navigation.domain.repository.NavigationButtonRepository
import com.ataglance.walletglance.personalization.domain.repository.WidgetRepository
import com.ataglance.walletglance.settings.data.repository.SettingsRepository

class DeleteAllDataLocallyUseCaseImpl(
    private val settingsRepository: SettingsRepository,
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository,
    private val categoryCollectionRepository: CategoryCollectionRepository,
    private val widgetRepository: WidgetRepository,
    private val navigationButtonRepository: NavigationButtonRepository,
    private val localUpdateTimeRepository: LocalUpdateTimeRepository
) : DeleteAllDataLocallyUseCase {
    override suspend fun execute() {
        settingsRepository.clearAllPreferences()
        accountRepository.deleteAllAccountsLocally()
        categoryRepository.deleteAllCategoriesLocally()
        categoryCollectionRepository.deleteAllCollectionsLocally()
        widgetRepository.deleteAllWidgetsLocally()
        navigationButtonRepository.deleteAllNavigationButtonsLocally()
        localUpdateTimeRepository.deleteAllUpdateTimes()
    }
}