package com.ataglance.walletglance.navigation.domain.usecase

import com.ataglance.walletglance.navigation.domain.repository.NavigationButtonRepository
import com.ataglance.walletglance.navigation.domain.model.AppScreenEnum

class SaveNavigationButtonsUseCaseImpl(
    private val navigationButtonRepository: NavigationButtonRepository
) : SaveNavigationButtonsUseCase {

    override suspend fun execute(screens: List<AppScreenEnum>) {
        val screens = screens.toMutableList()
            .apply {
                if (!contains(AppScreenEnum.Home)) add(0, AppScreenEnum.Home)
                if (!contains(AppScreenEnum.Settings)) add(AppScreenEnum.Settings)
            }

        navigationButtonRepository.upsertNavigationButtons(screens = screens)
    }

}