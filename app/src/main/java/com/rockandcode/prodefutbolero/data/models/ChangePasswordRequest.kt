package com.rockandcode.prodefutbolero.data.models

data class ChangePasswordRequest(
    val userId: String,
    val oldPassword: String,
    val newPassword: String,
)
