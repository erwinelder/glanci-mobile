package com.ataglance.walletglance.core.presentation.model.validation

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable

@Stable
data class ValidationState(
    val isValid: Boolean,
    @StringRes val messageRes: Int
)
