package com.ataglance.walletglance.core.presentation.preview

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.other.AppBackground
import com.ataglance.walletglance.core.presentation.theme.GlanciTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PreviewContainer(
    appTheme: AppTheme = AppTheme.LightDefault,
    content: @Composable BoxScope.() -> Unit
) {
    BoxWithConstraints {
        SharedTransitionLayout {
            GlanciTheme(
                useDeviceTheme = false,
                lastChosenTheme = appTheme,
                boxWithConstraintsScope = this@BoxWithConstraints,
                sharedTransitionScope = this@SharedTransitionLayout
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppBackground(appTheme = appTheme)
                    content()
                }
            }
        }
    }
}