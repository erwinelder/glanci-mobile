package com.ataglance.walletglance.navigation.domain.usecase

import com.ataglance.walletglance.navigation.domain.model.AppScreenEnum
import kotlinx.coroutines.flow.Flow

interface GetNavigationButtonScreensUseCase {

    fun getAsFlow(): Flow<List<AppScreenEnum>>

}