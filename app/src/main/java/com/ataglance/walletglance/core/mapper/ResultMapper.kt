package com.ataglance.walletglance.core.mapper

import com.ataglance.walletglance.core.domain.result.error.DomainError
import com.glanci.request.shared.ResultData
import com.glanci.request.shared.SimpleResult
import com.glanci.request.shared.error.DataError


fun <E1 : DataError, E2 : DomainError> SimpleResult<E1>.toDomainSimpleResult(
    errorMapper: (E1) -> E2
): com.ataglance.walletglance.core.domain.result.SimpleResult<E2> {
    return when (this) {
        is SimpleResult.Success -> com.ataglance.walletglance.core.domain.result.SimpleResult.Success()
        is SimpleResult.Error -> com.ataglance.walletglance.core.domain.result.SimpleResult.Error(
            error = errorMapper(error)
        )
    }
}


fun <D1, D2, E1 : DataError, E2 : DomainError> ResultData<D1, E1>.toDomainResultData(
    dataMapper: (D1) -> D2,
    errorMapper: (E1) -> E2
): com.ataglance.walletglance.core.domain.result.ResultData<D2, E2> {
    return when (this) {
        is ResultData.Success -> com.ataglance.walletglance.core.domain.result.ResultData.Success(
            data = dataMapper(data)
        )
        is ResultData.Error -> com.ataglance.walletglance.core.domain.result.ResultData.Error(
            error = errorMapper(error)
        )
    }
}