package com.ataglance.walletglance.navigation.domain.usecase

import com.ataglance.walletglance.navigation.domain.repository.NavigationButtonRepository
import com.ataglance.walletglance.navigation.domain.model.AppScreenEnum
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class GetNavigationButtonScreensUseCaseImpl(
    private val navigationButtonRepository: NavigationButtonRepository
) : GetNavigationButtonScreensUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAsFlow(): Flow<List<AppScreenEnum>> {
        return navigationButtonRepository.getAllNavigationButtonsAsFlow().mapLatest { buttons ->
            buttons.ifEmpty {
                AppScreenEnum.asDefaultNavigationButtonScreens().also {
                    navigationButtonRepository.upsertNavigationButtons(screens = it)
                }
            }
        }
    }

}