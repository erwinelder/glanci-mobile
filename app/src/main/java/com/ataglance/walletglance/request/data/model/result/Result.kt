package com.ataglance.walletglance.request.data.model.result

import com.ataglance.walletglance.request.data.model.result.error.DataError
import com.ataglance.walletglance.request.data.model.result.success.DataSuccess
import kotlinx.serialization.Serializable

@Serializable
sealed interface Result<out S: DataSuccess?, out E: DataError> {

    @Serializable
    data class Success<out S: DataSuccess?, out E: DataError>(val success: S): Result<S, E>

    @Serializable
    data class Error<out S: DataSuccess?, out E: DataError>(val error: E): Result<S, E>

}