package com.ataglance.walletglance.budget.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.budget.presentation.component.container.BudgetsOnWidgetSettingsBottomSheetPreview
import com.ataglance.walletglance.budget.presentation.model.BudgetUiState
import com.ataglance.walletglance.core.presentation.component.checkbox.TwoStateCheckbox

@Composable
fun CheckedBudgetComponent(
    budget: BudgetUiState,
    modifier: Modifier = Modifier,
    checked: Boolean,
    checkedEnabled: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    BasicBudgetComponent(
        budget = budget,
        onClick = {
            onCheckedChange(!checked)
        },
        modifier = modifier,
        clickEnabled = checkedEnabled
    ) {
        TwoStateCheckbox(
            checked = checked,
            enabled = checkedEnabled,
            onClick = onCheckedChange
        )
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun CheckedDefaultBudgetComponentPreview() {
    BudgetsOnWidgetSettingsBottomSheetPreview()
}