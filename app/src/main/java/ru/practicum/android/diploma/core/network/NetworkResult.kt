package ru.practicum.android.diploma.core.network

sealed interface NetworkResult<out T> {
    data class Success<T>(val data: T): NetworkResult<T>
    data class Error(val code: HttpCodes): NetworkResult<Nothing>
}
