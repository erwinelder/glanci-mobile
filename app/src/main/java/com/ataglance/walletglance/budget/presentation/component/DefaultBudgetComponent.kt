package com.ataglance.walletglance.budget.presentation.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.presentation.screen.EditBudgetsScreenPreview
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanciColors

@Composable
fun DefaultBudgetComponent(
    budget: Budget,
    onClick: (Budget) -> Unit
) {
    BasicDefaultBudgetComponent(
        budget = budget,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(R.drawable.short_arrow_right_icon),
            contentDescription = "right arrow icon",
            tint = GlanciColors.onSurface,
            modifier = Modifier.size(20.dp)
        )
    }
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun DefaultBudgetComponentPreview() {
    EditBudgetsScreenPreview(appTheme = AppTheme.LightDefault)
}