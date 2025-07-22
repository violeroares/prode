package com.rockandcode.prodefutbolero.data.repositories

import com.rockandcode.prodefutbolero.data.datasources.network.ApiService
import com.rockandcode.prodefutbolero.data.models.ChangePasswordRequest
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
            if (response.isSuccessful) {
                return response.body()?.token ?: throw Exception("Token vacío")
            } else {
                when (response.code()) {
                    400 -> throw Exception("Usuario o contraseña incorrectos")
                    401 -> throw Exception("Usuario o contraseña incorrectos")
                    500, 502, 503 -> throw Exception("Servidor temporalmente no disponible")
                    else -> throw Exception("Error del servidor ${response.code()} del servidor ${response.code()}")
                }
            }
        } catch (e: IOException) {
            throw Exception("Error de conexión. Verificá tu conexión a internet", e)
        }
    }

    override suspend fun getUserProfile(): User {
        try {
            val response = apiService.getUserProfile()
            if (response.isSuccessful) {
                return response.body()?.toDomain() ?: throw Exception("Perfil de usuario vacío")
            } else {
                when (response.code()) {
                    401 -> throw Exception("Token expirado o no válido")
                    in 500..599 -> throw Exception("Servidor temporalmente no disponible")
                    else -> throw Exception("Error del servidor ${response.code()} del servidor ${response.code()}")
                }
            }
        } catch (e: IOException) {
            throw Exception("Error de red")
        } catch (e: Exception) {
            throw Exception("Error desconocido: ${e.message}")
        }
    }

    override suspend fun changePassword(
        userId: String,
        oldPassword: String,
        newPassword: String,
    ): Boolean {
        try {
            return apiService
                .changePassword(
                    ChangePasswordRequest(
                        userId = userId,
                        oldPassword = oldPassword,
                        newPassword = newPassword,
                    ),
                ).isSuccessful
        } catch (e: IOException) {
            throw Exception("Error de red")
        } catch (e: Exception) {
            throw Exception("Error desconocido: ${e.message}")
        }
    }
}
