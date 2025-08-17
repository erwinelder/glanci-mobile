package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.validation.UserDataValidator
import com.ataglance.walletglance.auth.mapper.toResultStateButton
import com.ataglance.walletglance.auth.mapper.toUiStates
import com.ataglance.walletglance.auth.presentation.viewmodel.NameUpdateViewModel
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurface
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.model.icon.IconPathsRes
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.domain.result.success.AuthSuccess
import com.ataglance.walletglance.core.presentation.component.field.validation.ValidatedSmallTextFieldWithLabel
import com.ataglance.walletglance.core.presentation.component.screenContainer.request.AnimatedRequestScreenContainerWithTopNavBackButton
import com.ataglance.walletglance.core.presentation.model.request.RequestState
import com.ataglance.walletglance.core.presentation.model.result.ResultState.ButtonState
import com.ataglance.walletglance.core.presentation.model.validation.ValidatedFieldState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NameUpdateScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavHostController
) {
    val viewModel = koinViewModel<NameUpdateViewModel>()

    val nameState by viewModel.nameState.collectAsStateWithLifecycle()
    val nameUpdateIsAllowed by viewModel.nameUpdateIsAllowed.collectAsStateWithLifecycle()
    val requestState by viewModel.requestState.collectAsStateWithLifecycle()

    NameUpdateScreen(
        screenPadding = screenPadding,
        onNavigateBack = navController::popBackStack,
        nameState = nameState,
        onNameChange = viewModel::updateAndValidateName,
        nameUpdateIsAllowed = nameUpdateIsAllowed,
        onUpdateName = viewModel::updateName,
        requestState = requestState,
        onCancelRequest = viewModel::cancelNameUpdate,
        onSuccessButton = navController::popBackStack,
        onErrorButton = viewModel::resetRequestState
    )
}

@Composable
fun NameUpdateScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    nameState: ValidatedFieldState,
    onNameChange: (String) -> Unit,
    nameUpdateIsAllowed: Boolean,
    onUpdateName: () -> Unit,

    requestState: RequestState<ButtonState, ButtonState>?,
    onCancelRequest: () -> Unit,
    onSuccessButton: () -> Unit,
    onErrorButton: () -> Unit
) {
    val title = if (nameUpdateIsAllowed) {
        stringResource(R.string.update_your_name_to, nameState.fieldText)
    } else {
        stringResource(R.string.update_your_name)
    }

    AnimatedRequestScreenContainerWithTopNavBackButton(
        screenPadding = screenPadding,
        iconPathsRes = IconPathsRes.Username,
        title = title,
        requestStateButton = requestState,
        onCancelRequest = onCancelRequest,
        onSuccessButton = onSuccessButton,
        onErrorButton = onErrorButton,
        backButtonText = stringResource(R.string.update_name),
        onBackButtonClick = onNavigateBack,
        screenCenterContent = {
            GlassSurface(
                filledWidths = FilledWidthByScreenType(compact = .86f)
            ) {
                GlassSurfaceContent(
                    nameState = nameState,
                    onNameChange = onNameChange,
                    onUpdateName = onUpdateName
                )
            }
        },
        screenBottomContent = {
            PrimaryButton(
                text = stringResource(R.string.update_name),
                enabled = nameUpdateIsAllowed,
                onClick = onUpdateName
            )
        }
    )
}

@Composable
private fun GlassSurfaceContent(
    nameState: ValidatedFieldState,
    onNameChange: (String) -> Unit,
    onUpdateName: () -> Unit
) {
    GlassSurfaceContentColumnWrapper {
        ValidatedSmallTextFieldWithLabel(
            state = nameState,
            onValueChange = onNameChange,
            labelText = stringResource(R.string.new_name),
            placeholderText = stringResource(R.string.name),
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Go,
            onGoKeyboardAction = onUpdateName
        )
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun NameUpdateScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    val name = "New username"
    val nameState = ValidatedFieldState(
        fieldText = name,
        validationStates = UserDataValidator.validateName(name).toUiStates()
    )
    val nameUpdateIsAllowed = UserDataValidator.isValidName(name)

    val coroutineScope = rememberCoroutineScope()
    var job = remember<Job?> { null }
    val initialRequestState = null
//    val initialRequestState = RequestState.Loading<ButtonState, ButtonState>(
//        messageRes = R.string.updating_your_name_loader
//    )
    var requestState by remember {
        mutableStateOf<RequestState<ButtonState, ButtonState>?>(initialRequestState)
    }

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        NameUpdateScreen(
            onNavigateBack = {},
            nameState = nameState,
            onNameChange = {},
            nameUpdateIsAllowed = nameUpdateIsAllowed,
            onUpdateName = {
                job = coroutineScope.launch {
                    requestState = RequestState.Loading(
                        messageRes = R.string.updating_your_name_loader
                    )
                    delay(2000)
                    requestState = RequestState.Success(
                        state = AuthSuccess.NameUpdated.toResultStateButton()
                    )
                }
            },

            requestState = requestState,
            onCancelRequest = {
                requestState = initialRequestState
                job?.cancel()
            },
            onSuccessButton = { requestState = initialRequestState },
            onErrorButton = { requestState = initialRequestState }
        )
    }
}