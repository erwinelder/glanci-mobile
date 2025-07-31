package com.ataglance.walletglance.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.ataglance.walletglance.auth.domain.usecase.auth.SignOutUseCase

class SignOutViewModel(
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    suspend fun signOut() {
        signOutUseCase.execute()
    }

}