package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.ataglance.walletglance.auth.domain.model.user.UserContext
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.utils.getGreetingsWidgetTitleRes
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.presentation.component.NavigateToSettingsCategoryButton
import com.ataglance.walletglance.settings.presentation.model.SettingsCategory
import com.ataglance.walletglance.settings.presentation.screenContainer.SettingsCategoryScreenContainer
import org.koin.compose.koinInject

@Composable
fun ProfileScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavHostController,
    navViewModel: NavigationViewModel
) {
    val greetingsTitleRes by getGreetingsWidgetTitleRes()
    val userContext = koinInject<UserContext>()

    ProfileScreen(
        screenPadding = screenPadding,
        onNavigateBack = navController::popBackStack,
        greetingsTitle = stringResource(greetingsTitleRes, userContext.name ?: ""),
        onPopBackStackAndNavigateToScreen = { screen ->
            navViewModel.popBackStackAndNavigate(navController, screen)
        }
    )
}

@Composable
fun ProfileScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    greetingsTitle: String,
    onPopBackStackAndNavigateToScreen: (Any) -> Unit
) {
    val appTheme = CurrAppTheme
    val settingsCategory = SettingsCategory.Profile(appTheme)

    Box {
        SettingsCategoryScreenContainer(
            screenPadding = screenPadding,
            thisCategory = settingsCategory,
            onNavigateBack = onNavigateBack,
            title = greetingsTitle,
            mainScreenContent = {
                NavigateToSettingsCategoryButton(SettingsCategory.DeleteAccount(appTheme), onPopBackStackAndNavigateToScreen)
                NavigateToSettingsCategoryButton(SettingsCategory.SignOut(appTheme), onPopBackStackAndNavigateToScreen)
                NavigateToSettingsCategoryButton(SettingsCategory.UpdatePassword(appTheme), onPopBackStackAndNavigateToScreen)
                NavigateToSettingsCategoryButton(SettingsCategory.UpdateEmail(appTheme), onPopBackStackAndNavigateToScreen)
                NavigateToSettingsCategoryButton(SettingsCategory.UpdateName(appTheme), onPopBackStackAndNavigateToScreen)
//                NavigateToSettingsCategoryButton(SettingsCategory.ManageSubscriptions(appTheme), onPopBackStackAndNavigateToScreen)
            }
        )
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun ProfileScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        ProfileScreen(
            onNavigateBack = {},
            greetingsTitle = "Good afternoon, username!",
            onPopBackStackAndNavigateToScreen = {}
        )
    }
}