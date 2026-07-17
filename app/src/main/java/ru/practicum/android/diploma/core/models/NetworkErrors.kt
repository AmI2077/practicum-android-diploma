package ru.practicum.android.diploma.core.models

sealed interface NetworkErrors {
    data object ServerError : NetworkErrors
    data object NoInternetConnectionError : NetworkErrors
    data object NotFoundError : NetworkErrors
}
