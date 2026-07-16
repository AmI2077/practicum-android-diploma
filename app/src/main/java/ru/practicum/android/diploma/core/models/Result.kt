package ru.practicum.android.diploma.core.models

sealed interface Result<out T> {
    data class Content<T>(val data: T): Result<T>
    data class Error(val error: NetworkErrors): Result<Nothing>
}
