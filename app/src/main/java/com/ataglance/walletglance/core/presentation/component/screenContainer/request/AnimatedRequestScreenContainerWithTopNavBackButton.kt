package com.ataglance.walletglance.core.presentation.component.screenContainer.request

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.ataglance.walletglance.core.presentation.component.button.GlassSurfaceTopNavButtonBlock
import com.ataglance.walletglance.core.presentation.model.icon.IconPathsRes
import com.ataglance.walletglance.core.presentation.model.request.RequestErrorState
import com.ataglance.walletglance.core.presentation.model.request.RequestState
import com.ataglance.walletglance.core.presentation.model.result.ResultState.ButtonState

@Composable
fun AnimatedRequestScreenContainerWithTopNavBackButton(
    screenPadding: PaddingValues = PaddingValues(),
    iconPathsRes: IconPathsRes,
    title: String? = null,
    requestStateButton: RequestState<ButtonState, ButtonState>?,
    onCancelRequest: (() -> Unit)? = null,
    onSuccessButton: (() -> Unit)? = null,
    onErrorButton: () -> Unit,
    backButtonText: String,
    onBackButtonClick: () -> Unit,
    screenCenterContent: @Composable ((isKeyboardVisible: Boolean) -> Unit)? = null,
    screenBottomContent: @Composable (() -> Unit)? = null
) {
    AnimatedRequestScreenContainer(
        screenPadding = screenPadding,
        iconPathsRes = iconPathsRes,
        title = title,
        requestStateButton = requestStateButton,
        onCancelRequest = onCancelRequest,
        onSuccessButton = onSuccessButton,
        onErrorButton = onErrorButton,
        screenTopContent = {
            GlassSurfaceTopNavButtonBlock(
                text = backButtonText,
                imageRes = null,
                onClick = onBackButtonClick
            )
        },
        screenCenterContent = screenCenterContent,
        screenBottomContent = screenBottomContent
    )
}

@Composable
fun AnimatedRequestScreenContainerWithTopNavBackButton(
    screenPadding: PaddingValues = PaddingValues(),
    iconPathsRes: IconPathsRes,
    title: String? = null,
    requestErrorStateButton: RequestErrorState<ButtonState>?,
    onCancelRequest: (() -> Unit)? = null,
    onErrorButton: () -> Unit,
    backButtonText: String,
    onBackButtonClick: () -> Unit,
    screenCenterContent: @Composable ((isKeyboardVisible: Boolean) -> Unit)? = null,
    screenBottomContent: @Composable (() -> Unit)? = null
) {
    AnimatedRequestScreenContainer(
        screenPadding = screenPadding,
        iconPathsRes = iconPathsRes,
        title = title,
        requestErrorStateButton = requestErrorStateButton,
        onCancelRequest = onCancelRequest,
        onErrorButton = onErrorButton,
        screenTopContent = {
            GlassSurfaceTopNavButtonBlock(
                text = backButtonText,
                imageRes = null,
                onClick = onBackButtonClick
            )
        },
        screenCenterContent = screenCenterContent,
        screenBottomContent = screenBottomContent
    )
}