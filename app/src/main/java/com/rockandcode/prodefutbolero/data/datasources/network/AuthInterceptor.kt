package com.rockandcode.prodefutbolero.data.datasources.network

import com.rockandcode.prodefutbolero.domain.user.repository.ISessionRepository
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val sessionManager: ISessionRepository,
) : Interceptor {
    // https://square.github.io/okhttp/features/interceptors/
    // Retrofit interceptor documentación
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val token = sessionManager.userToken
        val requestBuilder = originalRequest.newBuilder()

        if (!token.isNullOrBlank()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
