package com.ataglance.walletglance.navigation.domain.repository

import com.ataglance.walletglance.navigation.domain.model.AppScreenEnum
import kotlinx.coroutines.flow.Flow

interface NavigationButtonRepository {

    suspend fun upsertNavigationButtons(screens: List<AppScreenEnum>)

    suspend fun deleteAllNavigationButtonsLocally()

    fun getAllNavigationButtonsAsFlow(): Flow<List<AppScreenEnum>>

}