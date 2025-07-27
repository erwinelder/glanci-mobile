package com.ataglance.walletglance.request.mapper.result

import com.ataglance.walletglance.request.data.model.result.ResultData
import com.ataglance.walletglance.request.data.model.result.SimpleResult
import com.ataglance.walletglance.request.data.model.result.error.DataError
import com.ataglance.walletglance.request.domain.model.result.DomainError


fun <E1 : DataError, E2 : DomainError> SimpleResult<E1>.toDomainSimpleResult(
    errorMapper: (E1) -> E2
): com.ataglance.walletglance.request.domain.model.result.SimpleResult<E2> {
    return when (this) {
        is SimpleResult.Success -> com.ataglance.walletglance.request.domain.model.result.SimpleResult.Success()
        is SimpleResult.Error -> com.ataglance.walletglance.request.domain.model.result.SimpleResult.Error(
            error = errorMapper(error)
        )
    }
}


fun <D1, D2, E1 : DataError, E2 : DomainError> ResultData<D1, E1>.toDomainResultData(
    dataMapper: (D1) -> D2,
    errorMapper: (E1) -> E2
): com.ataglance.walletglance.request.domain.model.result.ResultData<D2, E2> {
    return when (this) {
        is ResultData.Success -> com.ataglance.walletglance.request.domain.model.result.ResultData.Success(
            data = dataMapper(data)
        )
        is ResultData.Error -> com.ataglance.walletglance.request.domain.model.result.ResultData.Error(
            error = errorMapper(error)
        )
    }
}