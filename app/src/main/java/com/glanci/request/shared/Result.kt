package com.glanci.request.shared

import com.glanci.request.shared.error.DataError
import com.glanci.request.shared.success.DataSuccess
import kotlinx.serialization.Serializable

@Serializable
sealed interface Result<out S: DataSuccess?, out E: DataError> {

    @Serializable
    data class Success<out S: DataSuccess?, out E: DataError>(val success: S): Result<S, E>

    @Serializable
    data class Error<out S: DataSuccess?, out E: DataError>(val error: E): Result<S, E>

}