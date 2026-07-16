package ru.practicum.android.diploma.core.network.client

import okhttp3.Interceptor
import okhttp3.Response
import ru.practicum.android.diploma.core.network.NetworkHeaders

class AuthorizationInterceptor(
    private val token: String
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader(
                NetworkHeaders.AUTHORIZATION_HEADER,
                "${NetworkHeaders.BEARER_AUTHORIZATION} $token"
            )
            .addHeader(NetworkHeaders.CONTENT_TYPE_HEADER, NetworkHeaders.CONTENT_TYPE_JSON)
            .build()

        return chain.proceed(newRequest)
    }
}
