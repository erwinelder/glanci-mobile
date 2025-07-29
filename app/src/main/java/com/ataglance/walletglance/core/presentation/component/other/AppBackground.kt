package com.ataglance.walletglance.core.presentation.component.other

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme

@Composable
fun AppBackground(
    appTheme: AppTheme? = CurrAppTheme
) {
    AnimatedContent(
        targetState = appTheme,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        }
    ) { targetAppTheme ->
        if (targetAppTheme != null) {
            val imageAndDescription = when (targetAppTheme) {
                AppTheme.LightDefault -> R.drawable.main_background_light to
                        "application light background"
                AppTheme.DarkDefault -> R.drawable.main_background_dark to
                        "application dark background"
            }
            Image(
                painter = painterResource(imageAndDescription.first),
                contentDescription = imageAndDescription.second,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(modifier = Modifier.fillMaxSize())
        }
    }
}