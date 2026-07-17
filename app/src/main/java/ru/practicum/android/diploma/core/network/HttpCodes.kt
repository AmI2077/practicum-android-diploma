package ru.practicum.android.diploma.core.network

private const val SERVER_ERROR = 500
private const val NO_INTERNET_ERROR = -1
private const val NOT_FOUND_ERROR = 404

enum class HttpCodes(val code: Int) {
    SERVER_ERROR_CODE(SERVER_ERROR), NO_INTERNET_CONNECTION_ERROR_CODE(NO_INTERNET_ERROR), NOT_FOUND_ERROR_CODE(
        NOT_FOUND_ERROR
    );

    companion object {
        val codes = entries.associateBy(HttpCodes::code)

        fun fromInt(code: Int): HttpCodes {
            return codes[code] ?: SERVER_ERROR_CODE
        }
    }
}
