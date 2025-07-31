package com.ataglance.walletglance.navigation.domain.model

enum class AppScreenEnum {
    Home, Records, CategoryStatistics, Budgets, Settings;

    companion object {

        fun asDefaultNavigationButtonScreens(): List<AppScreenEnum> {
            return listOf(
                Home,
                Records,
                CategoryStatistics,
                Budgets,
                Settings
            )
        }

    }

}