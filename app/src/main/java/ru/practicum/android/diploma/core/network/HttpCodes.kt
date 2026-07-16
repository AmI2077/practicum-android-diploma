package ru.practicum.android.diploma.core.network

enum class HttpCodes(val code: Int) {
    SERVER_ERROR_CODE(500),
    NO_INTERNET_CONNECTION_ERROR_CODE(-1),
    NOT_FOUND_ERROR_CODE(404);

    companion object {
        fun fromInt(code: Int): HttpCodes {
            return entries.associateBy(HttpCodes::code)[code] ?: SERVER_ERROR_CODE
        }
    }
}
