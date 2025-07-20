package com.glanci.navigation.shared.service

import com.glanci.navigation.shared.dto.NavigationButtonDto
import kotlinx.rpc.annotations.Rpc

@Rpc
interface NavigationButtonService {

    suspend fun getUpdateTime(token: String): Long?

    suspend fun saveNavigationButtons(
        buttons: List<NavigationButtonDto>,
        timestamp: Long,
        token: String
    )

    suspend fun getNavigationButtonsAfterTimestamp(
        timestamp: Long,
        token: String
    ): List<NavigationButtonDto>?

    suspend fun saveNavigationButtonsAndGetAfterTimestamp(
        buttons: List<NavigationButtonDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): List<NavigationButtonDto>?

}