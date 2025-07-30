package com.ataglance.walletglance.budget.presentation.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.budget.presentation.component.container.BudgetsOnWidgetSettingsBottomSheetPreview
import com.ataglance.walletglance.budget.presentation.model.GroupedCheckedBudgetsItemUiState
import com.ataglance.walletglance.core.presentation.component.divider.TextDivider

@Composable
fun GroupedCheckedBudgetsComponent(
    groupedBudgetsItems: List<GroupedCheckedBudgetsItemUiState>,
    limitIsReached: Boolean,
    onCheckBudget: (Int) -> Unit,
    onUncheckBudget: (Int) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = groupedBudgetsItems) { item ->
            when (item) {
                is GroupedCheckedBudgetsItemUiState.RepeatingPeriodText -> {
                    TextDivider(
                        modifier = Modifier
                            .fillMaxWidth(.9f)
                            .padding(top = 8.dp),
                        textRes = item.stringRes
                    )
                }
                is GroupedCheckedBudgetsItemUiState.Budget -> {
                    val alpha by animateFloatAsState(
                        targetValue = if (!limitIsReached || item.checked) 1f else 0.5f
                    )
                    CheckedBudgetComponent(
                        budget = item.uiState,
                        modifier = Modifier.alpha(alpha),
                        checked = item.checked,
                        checkedEnabled = !limitIsReached || item.checked
                    ) {
                        if (item.checked) {
                            onUncheckBudget(item.uiState.id)
                        } else {
                            onCheckBudget(item.uiState.id)
                        }
                    }
                }
            }
        }
    }
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun GroupedCheckedBudgetsComponentPreview() {
    BudgetsOnWidgetSettingsBottomSheetPreview()
}