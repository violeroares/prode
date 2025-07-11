package com.rockandcode.prodefutbolero.domain.user.usecases

import com.rockandcode.prodefutbolero.domain.user.models.User
import com.rockandcode.prodefutbolero.domain.user.repository.ISessionRepository
import kotlinx.coroutines.flow.Flow

class GetUserSessionUseCase(
    private val sessionManager: ISessionRepository,
) {
    operator fun invoke(): Flow<User?> = sessionManager.currentUser
}
