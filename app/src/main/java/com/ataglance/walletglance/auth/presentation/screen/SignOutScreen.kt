package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.presentation.viewmodel.SignOutViewModel
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.screenContainer.ScreenContainerWithTopBackNavButton
import com.ataglance.walletglance.core.presentation.component.screenContainer.result.ResultScreenContainer
import com.ataglance.walletglance.core.presentation.model.icon.IconPathsRes
import com.ataglance.walletglance.core.presentation.preview.PreviewContainer
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.presentation.navigation.SettingsScreens
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignOutScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavHostController,
    navViewModel: NavigationViewModel
) {
    val viewModel = koinViewModel<SignOutViewModel>()

    val coroutineScope = rememberCoroutineScope()

    SignOutScreen(
        screenPadding = screenPadding,
        onNavigateBack = navController::popBackStack,
        onSignOut = {
            coroutineScope.launch {
                viewModel.signOut()
                navViewModel.navigateAndPopUpTo(
                    navController = navController, screenToNavigateTo = SettingsScreens.Start
                )
            }
        }
    )
}

@Composable
fun SignOutScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    onSignOut: () -> Unit
) {
    ScreenContainerWithTopBackNavButton(
        screenPadding = screenPadding,
        bottomPadding = 24.dp,
        backNavButtonText = stringResource(R.string.sign_out),
        backNavButtonImageRes = null,
        onBackNavButtonClick = onNavigateBack
    ) {
        ResultScreenContainer(
            padding = PaddingValues(),
            iconPathsRes = IconPathsRes.SignOut,
            iconGradientColor = GlanciColors.iconPrimaryGlassGradientPair,
            title = stringResource(R.string.sign_out_title),
            message = stringResource(R.string.sign_out_message),
            buttonText = stringResource(R.string.sign_out),
            onPrimaryButtonClick = onSignOut
        )
    }
}



@Preview(device = PIXEL_7_PRO)
@Composable
fun SignOutScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    PreviewContainer(appTheme = appTheme) {
        SignOutScreen(
            onNavigateBack = {},
            onSignOut = {}
        )
    }
}