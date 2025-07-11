package com.rockandcode.prodefutbolero.di

import com.rockandcode.prodefutbolero.data.datasources.network.ApiService
import com.rockandcode.prodefutbolero.data.repositories.TournamentRepository
import com.rockandcode.prodefutbolero.data.repositories.UserRepository
import com.rockandcode.prodefutbolero.domain.tournament.repository.ITournamentRepository
import com.rockandcode.prodefutbolero.domain.user.repository.IUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    // --- Repositorios ---
    @Provides @Singleton
    fun providerUserRepository(apiService: ApiService): IUserRepository = UserRepository(apiService)

    @Provides @Singleton
    fun providerTournamentRepository(apiService: ApiService): ITournamentRepository = TournamentRepository(apiService)

//    @Provides @Singleton
//    fun providerMatchRepository(apiService: ApiService): IMatchRepository = MatchRepository(apiService)
}
