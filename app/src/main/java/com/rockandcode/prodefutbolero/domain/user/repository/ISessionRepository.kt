package com.rockandcode.prodefutbolero.domain.user.repository

import com.rockandcode.prodefutbolero.domain.user.models.User
import kotlinx.coroutines.flow.Flow

interface ISessionRepository {
    val currentUser: Flow<User?>

    var userToken: String?

    suspend fun saveUser(user: User)

    suspend fun clearSession()
}
