package com.ataglance.walletglance.request.domain.model.result

import com.ataglance.walletglance.request.domain.model.result.error.DomainError
import com.ataglance.walletglance.request.domain.model.result.success.DomainSuccess

sealed interface Result<out S: DomainSuccess?, out E: DomainError> {
    data class Success<out S: DomainSuccess?, out E: DomainError>(val success: S): Result<S, E>
    data class Error<out S: DomainSuccess?, out E: DomainError>(val error: E): Result<S, E>
}