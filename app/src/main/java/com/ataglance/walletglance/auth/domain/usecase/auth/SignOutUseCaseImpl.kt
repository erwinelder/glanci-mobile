package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.auth.domain.model.user.UserContext
import com.ataglance.walletglance.core.domain.usecase.DeleteAllDataLocallyUseCase

class SignOutUseCaseImpl(
    private val userContext: UserContext,
    private val deleteAllDataLocallyUseCase: DeleteAllDataLocallyUseCase
) : SignOutUseCase {

    override suspend fun execute() {
        userContext.deleteData()
        deleteAllDataLocallyUseCase.execute()
    }

}