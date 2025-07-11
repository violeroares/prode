package com.rockandcode.prodefutbolero.domain.user.usecases

import com.rockandcode.prodefutbolero.domain.user.repository.ISessionRepository

class ClearSessionUseCase(
    private val sessionManager: ISessionRepository,
) {
    suspend operator fun invoke() {
        sessionManager.clearSession()
    }
}
