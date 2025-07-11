package com.rockandcode.prodefutbolero.domain.user.repository

import com.rockandcode.prodefutbolero.domain.user.models.User

interface IUserRepository {
    suspend fun login(
        email: String,
        password: String,
    ): String

    suspend fun getUserProfile(): User
}
