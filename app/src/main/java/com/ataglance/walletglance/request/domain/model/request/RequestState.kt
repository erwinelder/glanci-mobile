package com.ataglance.walletglance.request.domain.model.request

import com.ataglance.walletglance.request.domain.model.result.error.DomainError
import com.ataglance.walletglance.request.domain.model.result.success.DomainSuccess

sealed class RequestState <S : DomainSuccess, E : DomainError> {

    class Loading<S : DomainSuccess, E : DomainError> : RequestState<S, E>()

    data class Success<S : DomainSuccess, E : DomainError>(val result: S): RequestState<S, E>()

    data class Error<S : DomainSuccess, E : DomainError>(val result: E): RequestState<S, E>()

}