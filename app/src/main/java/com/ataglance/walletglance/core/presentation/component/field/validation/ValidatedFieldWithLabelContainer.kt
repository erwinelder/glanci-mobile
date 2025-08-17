package com.ataglance.walletglance.core.presentation.component.field.validation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.presentation.component.field.FieldLabel
import com.ataglance.walletglance.core.presentation.model.validation.ValidatedFieldState

@Composable
fun ValidatedFieldWithLabelContainer(
    state: ValidatedFieldState,
    labelText: String? = null,
    field: @Composable (fieldText: String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        labelText?.let { FieldLabel(text = it) }
        field(state.fieldText)
        FieldValidationMessages(validationStates = state.validationStates)
    }
}