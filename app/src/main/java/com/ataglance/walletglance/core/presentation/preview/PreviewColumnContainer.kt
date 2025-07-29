package com.ataglance.walletglance.core.presentation.preview

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.other.AppBackground
import com.ataglance.walletglance.core.presentation.theme.GlanciTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PreviewColumnContainer(
    appTheme: AppTheme = AppTheme.LightDefault,
    gap: Dp = 24.dp,
    content: @Composable ColumnScope.() -> Unit
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
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(gap)
                    ) {
                        content()
                    }
                }
            }
        }
    }
}