package com.ataglance.walletglance.request.domain.model.result

sealed interface ResultData<out D, out E: DomainError> {

    data class Success<out D, out E: DomainError>(val data: D): ResultData<D, E>

    data class Error<out D, out E: DomainError>(val error: E): ResultData<D, E>


    fun getDataOrNull(): D? = (this as? Success)?.data
    fun getErrorOrNull(): E? = (this as? Error)?.error


    fun <R> mapData(transform: (D) -> R): ResultData<R, E> {
        return when (this) {
            is Success -> Success<R, E>(data = transform(this.data))
            is Error -> Error(error = this.error)
        }
    }

    fun <R : DomainError> mapError(transform: (E) -> R): ResultData<D, R> {
        return when (this) {
            is Success -> Success(data = this.data)
            is Error -> Error<D, R>(error = transform(this.error))
        }
    }


    fun <S : DomainSuccess> toResult(success: S): Result<S, E> {
        return when (this) {
            is Success -> Result.Success(success = success)
            is Error -> Result.Error(error = this.error)
        }
    }

    fun toSimpleResult(): SimpleResult<E> {
        return when (this) {
            is Success -> SimpleResult.Success()
            is Error -> SimpleResult.Error(error = this.error)
        }
    }

}