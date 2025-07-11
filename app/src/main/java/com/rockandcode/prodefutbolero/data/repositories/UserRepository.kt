package com.rockandcode.prodefutbolero.data.repositories

import android.util.Log
import com.rockandcode.prodefutbolero.data.datasources.network.ApiService
import com.rockandcode.prodefutbolero.data.models.LoginRequest
import com.rockandcode.prodefutbolero.domain.user.models.User
import com.rockandcode.prodefutbolero.domain.user.repository.IUserRepository
import java.io.IOException

class UserRepository(
    private val apiService: ApiService,
) : IUserRepository {
    override suspend fun login(
        email: String,
        password: String,
    ): String {
        try {
            val response = apiService.login(LoginRequest(email, password))
            Log.d("UserRepository", "$email $password")
            if (response.isSuccessful) {
                return response.body()?.token ?: throw Exception("Token vacío")
            } else {
                Log.d("UserRepository", "${response.message()}")
                when (response.code()) {
                    400 -> throw Exception("Usuario o contraseña incorrectos")
                    401 -> throw Exception("Usuario o contraseña incorrectos")
                    500, 502, 503 -> throw Exception("Servidor temporalmente no disponible")
                    else -> throw Exception("Error del servidor ${response.code()}")
                }
            }
        } catch (e: IOException) {
            throw Exception("Error de conexión. Verificá tu conexión a internet", e)
        }
    }

    override suspend fun getUserProfile(): User =
        try {
            val responseDto = apiService.getUserProfile()
            responseDto.toDomain()
        } catch (e: Exception) {
            throw e
        }
}
