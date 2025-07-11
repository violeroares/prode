package com.rockandcode.prodefutbolero.data.models

data class LoginResponse(
    val name: String,
    val email: String,
    val token: String,
)
