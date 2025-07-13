package com.rockandcode.prodefutbolero.domain.user.models

data class User(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val avatarUrl: String,
)
