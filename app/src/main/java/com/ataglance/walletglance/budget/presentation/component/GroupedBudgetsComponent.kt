package com.ataglance.walletglance.budget.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.budget.presentation.model.BudgetUiState
import com.ataglance.walletglance.budget.presentation.model.GroupedBudgetsItemUiState
import com.ataglance.walletglance.budget.presentation.screen.EditBudgetsScreenPreview
import com.ataglance.walletglance.core.presentation.component.divider.TextDivider

@Composable
fun GroupedBudgetsComponent(
    groupedBudgetsItems: List<GroupedBudgetsItemUiState>,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    onBudgetClick: (BudgetUiState) -> Unit
) {
    LazyColumn(
        contentPadding = contentPadding,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = groupedBudgetsItems) { item ->
            when (item) {
                is GroupedBudgetsItemUiState.RepeatingPeriodText -> {
                    TextDivider(
                        modifier = Modifier
                            .fillMaxWidth(.9f)
                            .padding(top = 8.dp),
                        textRes = item.stringRes
                    )
                }
                is GroupedBudgetsItemUiState.Budget -> {
                    BudgetComponent(budget = item.uiState, onClick = onBudgetClick)
                }
            }
        }
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun BudgetsByPeriodComponentPreview() {
    EditBudgetsScreenPreview()
}