package com.ataglance.walletglance.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.user.UserContext
import com.ataglance.walletglance.auth.domain.model.validation.UserDataValidator
import com.ataglance.walletglance.auth.domain.usecase.auth.UpdateNameUseCase
import com.ataglance.walletglance.auth.mapper.toResultStateButton
import com.ataglance.walletglance.auth.mapper.toUiStates
import com.ataglance.walletglance.core.domain.result.Result
import com.ataglance.walletglance.core.domain.result.error.AuthError
import com.ataglance.walletglance.core.domain.result.success.AuthSuccess
import com.ataglance.walletglance.core.presentation.model.request.RequestState
import com.ataglance.walletglance.core.presentation.model.result.ResultState.ButtonState
import com.ataglance.walletglance.core.presentation.model.validation.ValidatedFieldState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NameUpdateViewModel(
    private val userContext: UserContext,
    private val updateNameUseCase: UpdateNameUseCase,
) : ViewModel() {

    /* ---------- Fields' states ---------- */

    private val _nameState = MutableStateFlow(
        ValidatedFieldState(
            validationStates = UserDataValidator.validateName(userContext.name ?: "").toUiStates()
        )
    )
    val nameState = _nameState.asStateFlow()

    fun updateAndValidateName(name: String) {
        _nameState.update {
            it.copy(
                fieldText = name,
                validationStates = UserDataValidator.validateName(name).toUiStates()
            )
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    val nameUpdateIsAllowed = _nameState.mapLatest {
        UserDataValidator.isValidName(name = it.fieldText)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )


    /* ---------- Update request state ---------- */

    private var updateNameJob: Job? = null

    fun updateName() {
        if (!nameUpdateIsAllowed.value) return

        setRequestLoadingState()

        updateNameJob = viewModelScope.launch {
            val name = _nameState.value.trimmedText
            val result = updateNameUseCase.execute(name = name)

            setRequestResultState(result = result)
            if (result is Result.Success) {
                userContext.updateName(name = name)
            }
        }
    }

    fun cancelNameUpdate() {
        updateNameJob?.cancel()
        updateNameJob = null
        resetRequestState()
    }


    private val _requestState = MutableStateFlow<RequestState<ButtonState, ButtonState>?>(null)
    val requestState = _requestState.asStateFlow()

    private fun setRequestLoadingState() {
        _requestState.update {
            RequestState.Loading(messageRes = R.string.updating_your_name_loader)
        }
    }

    private fun setRequestResultState(result: Result<AuthSuccess, AuthError>) {
        _requestState.update {
            when (result) {
                is Result.Success -> RequestState.Success(
                    state = result.success.toResultStateButton()
                )
                is Result.Error -> RequestState.Error(
                    state = result.error.toResultStateButton()
                )
            }
        }
    }

    fun resetRequestState() {
        _requestState.update { null }
    }

}