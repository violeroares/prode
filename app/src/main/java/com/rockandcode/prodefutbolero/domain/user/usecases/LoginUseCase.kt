package com.rockandcode.prodefutbolero.domain.user.usecases

import com.rockandcode.prodefutbolero.domain.user.repository.ISessionRepository
import com.rockandcode.prodefutbolero.domain.user.repository.IUserRepository

class LoginUseCase(
    private val userRepository: IUserRepository,
    private val sessionManager: ISessionRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
    ) {
        val token = userRepository.login(email, password)
        sessionManager.userToken = token

        val user = userRepository.getUserProfile()
        sessionManager.saveUser(user)
    }
}
