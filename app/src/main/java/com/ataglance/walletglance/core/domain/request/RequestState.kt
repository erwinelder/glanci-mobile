package com.ataglance.walletglance.core.domain.request

import com.ataglance.walletglance.core.domain.result.error.DomainError
import com.ataglance.walletglance.core.domain.result.success.DomainSuccess

sealed class RequestState <S : DomainSuccess, E : DomainError> {

    class Loading<S : DomainSuccess, E : DomainError> : RequestState<S, E>()

    data class Success<S : DomainSuccess, E : DomainError>(val result: S): RequestState<S, E>()

    data class Error<S : DomainSuccess, E : DomainError>(val result: E): RequestState<S, E>()

}