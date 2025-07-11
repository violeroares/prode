package com.rockandcode.prodefutbolero.di

import android.content.Context
import com.rockandcode.prodefutbolero.data.datasources.local.SessionManager
import com.rockandcode.prodefutbolero.data.datasources.network.ApiService
import com.rockandcode.prodefutbolero.data.datasources.network.AuthInterceptor
import com.rockandcode.prodefutbolero.data.datasources.network.RetrofitService
import com.rockandcode.prodefutbolero.domain.user.repository.ISessionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    // --- Proveedores de Infraestructura ---
    @Provides @Singleton
    fun providerAuthToken(
        @ApplicationContext context: Context,
    ): ISessionRepository = SessionManager(context)

    @Provides @Singleton
    fun providerAuthInterceptor(sessionManager: ISessionRepository): AuthInterceptor = AuthInterceptor(sessionManager)

    @Provides @Singleton
    fun providerApiService(sessionManager: ISessionRepository): ApiService =
        RetrofitService.createApiService(
            sessionManager,
        )
}
