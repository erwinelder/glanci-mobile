package com.ataglance.walletglance.core.presentation.component.divider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope

@Composable
fun TextDivider(
    textRes: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Spacer(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(GlanciColors.outlineSemiTransparent)
        )
        Text(
            text = stringResource(textRes),
            color = GlanciColors.outline,
            fontSize = 17.sp,
            fontWeight = FontWeight.W500,
            fontFamily = Manrope,
            modifier = Modifier
                .requiredWidth(IntrinsicSize.Min)
                .padding(horizontal = 8.dp)
        )
        Spacer(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(GlanciColors.outlineSemiTransparent)
        )
    }
}