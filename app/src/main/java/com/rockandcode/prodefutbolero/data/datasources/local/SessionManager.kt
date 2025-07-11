package com.rockandcode.prodefutbolero.data.datasources.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.rockandcode.prodefutbolero.domain.user.models.User
import com.rockandcode.prodefutbolero.domain.user.repository.ISessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionManager(
    context: Context,
) : ISessionRepository {
    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _currentUser = MutableStateFlow(loadCachedUser())
    override val currentUser: Flow<User?> = _currentUser.asStateFlow()

    override var userToken: String?
        get() = prefs.getString("Authorization", null)
        set(value) = prefs.edit { putString("Authorization", value) }

    override suspend fun saveUser(user: User) {
        prefs.edit { putString("CachedUser", gson.toJson(user)) }
        _currentUser.value = user
    }

    override suspend fun clearSession() {
        prefs.edit { clear() }
        _currentUser.value = null
    }

    private fun loadCachedUser(): User? {
        val json = prefs.getString("CachedUser", null)
        return json?.let { gson.fromJson(it, User::class.java) }
    }
}
