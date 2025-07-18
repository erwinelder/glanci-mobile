package com.ataglance.walletglance.request.presentation.component.field

import androidx.compose.animation.AnimatedContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope
import com.ataglance.walletglance.request.presentation.model.ValidationState

@Composable
fun FieldValidationMessage(
    state: ValidationState,
    fontSize: TextUnit = 15.sp
) {
    AnimatedContent(targetState = state) { targetState ->
        Text(
            text = "* " + stringResource(targetState.messageRes),
            fontSize = fontSize,
            color = if (targetState.isValid) GlanciColors.success else GlanciColors.error,
            fontWeight = FontWeight.W400,
            fontFamily = Manrope,
            textAlign = TextAlign.Center
        )
    }
}