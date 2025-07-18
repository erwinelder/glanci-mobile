package com.ataglance.walletglance.account.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.WindowTypeIsCompact
import com.ataglance.walletglance.core.presentation.theme.WindowTypeIsMedium
import com.ataglance.walletglance.core.presentation.component.container.DialogComponent

@Composable
fun AccountPicker(
    visible: Boolean,
    accountList: List<Account>,
    onDismissRequest: () -> Unit,
    onAccountChoose: (Account) -> Unit,
    lazyListState: LazyListState = rememberLazyListState()
) {
    DialogComponent(
        visible = visible,
        onDismissRequest = onDismissRequest
    ) {
        LazyColumn(
            state = lazyListState,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(dimensionResource(R.dimen.dialog_corner_size)))
                .background(GlanciColors.surface)
                .fillMaxWidth(
                    when {
                        WindowTypeIsCompact -> 1f
                        WindowTypeIsMedium -> .7f
                        else -> .6f
                    }
                )
        ) {
            items(
                items = accountList,
                key = { it.orderNum }
            ) { account ->
                SmallAccountComponent(account = account) {
                    onAccountChoose(account)
                    onDismissRequest()
                }
            }
        }
    }
}