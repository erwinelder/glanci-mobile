package com.ataglance.walletglance.budget.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.budget.presentation.model.FilledBudgetUiState
import com.ataglance.walletglance.budget.presentation.model.GroupedFilledBudgetsItemUiState
import com.ataglance.walletglance.budget.presentation.screen.EditBudgetsScreenPreview
import com.ataglance.walletglance.core.presentation.component.divider.TextDivider
import com.ataglance.walletglance.core.presentation.model.ResourceManager

@Composable
fun GroupedFilledBudgetsComponent(
    groupedBudgetsItems: List<GroupedFilledBudgetsItemUiState>,
    resourceManager: ResourceManager,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    textDividerFilledWidth: Float = .9f,
    onBudgetClick: (FilledBudgetUiState) -> Unit
) {
    LazyColumn(
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = groupedBudgetsItems) { item ->
            when (item) {
                is GroupedFilledBudgetsItemUiState.RepeatingPeriodText -> {
                    TextDivider(
                        modifier = Modifier
                            .fillMaxWidth(textDividerFilledWidth)
                            .padding(top = 8.dp),
                        textRes = item.stringRes
                    )
                }
                is GroupedFilledBudgetsItemUiState.Budget -> {
                    FilledBudgetGlassComponent(
                        budget = item.uiState,
                        resourceManager = resourceManager,
                        onClick = onBudgetClick
                    )
                }
            }
        }
    }
}



@Preview(device = PIXEL_7_PRO)
@Composable
private fun GroupedFilledBudgetsComponent() {
    EditBudgetsScreenPreview()
}