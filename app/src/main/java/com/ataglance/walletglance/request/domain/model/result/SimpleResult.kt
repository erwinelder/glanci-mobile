package com.ataglance.walletglance.request.domain.model.result

sealed interface SimpleResult<out E: DomainError> {

    class Success<out E: DomainError>: SimpleResult<E>
    data class Error<out E: DomainError>(val error: E): SimpleResult<E>


    fun getErrorOrNull(): E? = (this as? Error)?.error


    fun <R : DomainError> mapError(transform: (E) -> R): SimpleResult<R> {
        return when (this) {
            is Success -> Success()
            is Error -> Error<R>(error = transform(this.error))
        }
    }


    fun <S : DomainSuccess> toResult(success: S): Result<S, E> {
        return when (this) {
            is Success -> Result.Success(success = success)
            is Error -> Result.Error(error = error)
        }
    }

}

inline fun <E : DomainError> SimpleResult<E>.returnIfError(onReturn: (E) -> Nothing) {
    if (this is SimpleResult.Error) onReturn(this.error)
}

inline fun <E : DomainError> SimpleResult<E>.returnItIfError(onReturn: (SimpleResult<E>) -> Nothing) {
    if (this is SimpleResult.Error) onReturn(this)
}