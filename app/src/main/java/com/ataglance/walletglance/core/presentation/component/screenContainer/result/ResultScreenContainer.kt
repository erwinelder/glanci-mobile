package com.ataglance.walletglance.core.presentation.component.screenContainer.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.icon.AnimatedIconWithTitle
import com.ataglance.walletglance.core.presentation.component.screenContainer.ScreenContainer
import com.ataglance.walletglance.core.presentation.model.RotatingGradientAnimState
import com.ataglance.walletglance.core.presentation.model.icon.IconPathsRes
import com.ataglance.walletglance.core.presentation.theme.CurrWindowType
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope

@Composable
fun ResultScreenContainer(
    screenPadding: PaddingValues = PaddingValues(),
    padding: PaddingValues = PaddingValues(vertical = 24.dp),
    iconPathsRes: IconPathsRes,
    iconGradientColor: Pair<Color, Color>,
    title: String,
    message: String? = null,
    buttonText: String,
    onPrimaryButtonClick: () -> Unit
) {
    ScreenContainer(
        screenPadding = screenPadding,
        padding = padding
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth(FilledWidthByScreenType(.86f).get(CurrWindowType))
            ) {
                AnimatedIconWithTitle(
                    iconPathsRes = iconPathsRes,
                    title = title,
                    animState = RotatingGradientAnimState.Calm,
                    iconGradientColor = iconGradientColor,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                )
                if (message != null) {
                    Text(
                        text = message,
                        color = GlanciColors.onSurface,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.W400,
                        fontFamily = Manrope,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        PrimaryButton(
            text = buttonText,
            onClick = onPrimaryButtonClick
        )
    }
}