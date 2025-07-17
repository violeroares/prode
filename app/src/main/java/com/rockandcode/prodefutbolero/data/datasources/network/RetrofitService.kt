package com.rockandcode.prodefutbolero.data.datasources.network

import com.rockandcode.prodefutbolero.domain.user.repository.ISessionRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {
    // https://square.github.io/retrofit/
    // Documentacion de Retrofit
    fun createApiService(sessionManager: ISessionRepository): ApiService {
        val interceptor = AuthInterceptor(sessionManager)

//        val loggingInterceptor =
//            HttpLoggingInterceptor().apply {
//                level = HttpLoggingInterceptor.Level.BODY
//            }

        // Agregamos el interceptor
        val client =
            OkHttpClient
                .Builder()
                // .addInterceptor(loggingInterceptor)
                .addInterceptor(interceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

        // Builder Como lo vimos en clase
        return Retrofit
            .Builder()
            .baseUrl("http://200.32.43.207:3004/")
            // .baseUrl("http://172.16.128.102:5068/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
