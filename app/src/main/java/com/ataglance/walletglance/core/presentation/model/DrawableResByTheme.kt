package com.ataglance.walletglance.core.presentation.model

import androidx.annotation.DrawableRes
import com.ataglance.walletglance.core.domain.app.AppTheme

data class DrawableResByTheme(
    @DrawableRes val lightDefault: Int,
    @DrawableRes val darkDefault: Int
) {

    fun get(theme: AppTheme?): Int {
        return when (theme) {
            AppTheme.DarkDefault -> darkDefault
            else -> lightDefault
        }
    }

}