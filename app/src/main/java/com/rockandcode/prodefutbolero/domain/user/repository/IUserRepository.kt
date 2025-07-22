package com.rockandcode.prodefutbolero.domain.user.repository

import com.rockandcode.prodefutbolero.domain.user.models.User

interface IUserRepository {
    suspend fun login(
        email: String,
        password: String,
    ): String

    suspend fun getUserProfile(): User

    suspend fun changePassword(
        userId: String,
        oldPassword: String,
        newPassword: String,
    ): Boolean
}
