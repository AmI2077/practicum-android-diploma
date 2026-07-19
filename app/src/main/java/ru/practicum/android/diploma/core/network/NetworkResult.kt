package ru.practicum.android.diploma.core.network

import ru.practicum.android.diploma.core.models.NetworkErrors

sealed interface NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>
    data class Error(val code: HttpCodes) : NetworkResult<Nothing>
}

fun NetworkResult.Error.codeToError(): NetworkErrors {
    return when (code) {
        HttpCodes.NO_INTERNET_CONNECTION_ERROR_CODE -> NetworkErrors.NoInternetConnectionError
        HttpCodes.SERVER_ERROR_CODE -> NetworkErrors.ServerError
        HttpCodes.NOT_FOUND_ERROR_CODE -> NetworkErrors.NotFoundError
    }
}
