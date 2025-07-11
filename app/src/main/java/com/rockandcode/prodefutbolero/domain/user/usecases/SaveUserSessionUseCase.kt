package com.rockandcode.prodefutbolero.domain.user.usecases

import com.rockandcode.prodefutbolero.domain.user.models.User
import com.rockandcode.prodefutbolero.domain.user.repository.ISessionRepository

class SaveUserSessionUseCase(
    private val sessionManager: ISessionRepository,
) {
    suspend operator fun invoke(user: User) {
        sessionManager.saveUser(user)
    }
}
