package com.rockandcode.prodefutbolero.di

import com.rockandcode.prodefutbolero.domain.tournament.repository.ITournamentRepository
import com.rockandcode.prodefutbolero.domain.tournament.usecases.GetMatchesUseCase
import com.rockandcode.prodefutbolero.domain.tournament.usecases.GetTournamentHomeUseCase
import com.rockandcode.prodefutbolero.domain.user.repository.ISessionRepository
import com.rockandcode.prodefutbolero.domain.user.repository.IUserRepository
import com.rockandcode.prodefutbolero.domain.user.usecases.ClearSessionUseCase
import com.rockandcode.prodefutbolero.domain.user.usecases.GetUserSessionUseCase
import com.rockandcode.prodefutbolero.domain.user.usecases.LoginUseCase
import com.rockandcode.prodefutbolero.domain.user.usecases.LogoutUseCase
import com.rockandcode.prodefutbolero.domain.user.usecases.SaveUserSessionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideLoginUseCase(
        repo: IUserRepository,
        sessionManager: ISessionRepository,
    ): LoginUseCase = LoginUseCase(repo, sessionManager)

    @Provides
    fun provideClearSessionUseCase(sessionManager: ISessionRepository): ClearSessionUseCase = ClearSessionUseCase(sessionManager)

    @Provides
    fun provideGetUserSessionUseCase(sessionManager: ISessionRepository): GetUserSessionUseCase = GetUserSessionUseCase(sessionManager)

    @Provides
    fun provideSaveUserSessionUseCase(sessionManager: ISessionRepository): SaveUserSessionUseCase = SaveUserSessionUseCase(sessionManager)

    @Provides
    fun provideLogoutUseCase(sessionManager: ISessionRepository): LogoutUseCase = LogoutUseCase(sessionManager)

    @Provides
    fun provideGetTournamentHomeUseCase(repo: ITournamentRepository): GetTournamentHomeUseCase = GetTournamentHomeUseCase(repo)

    @Provides
    fun provideGetMatchesUseCase(repo: ITournamentRepository): GetMatchesUseCase = GetMatchesUseCase(repo)
}
