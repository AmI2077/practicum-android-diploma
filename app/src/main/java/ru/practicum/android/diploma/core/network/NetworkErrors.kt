package ru.practicum.android.diploma.core.network

enum class NetworkErrors(val code: Int) {

    SERVER_ERROR_CODE(500),
    NO_INTERNET_CONNECTION_ERROR(-1),
    NOT_FOUND_ERROR_CODE(404)
}
