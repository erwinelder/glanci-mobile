package com.ataglance.walletglance.auth.domain.model

data class CurrentAppVersion(
    val primary: Int,
    val secondary: Int,
    val tertiary: Int,
    val alpha: Int? = null,
    val beta: Int? = null,
    val rc: Int? = null
)