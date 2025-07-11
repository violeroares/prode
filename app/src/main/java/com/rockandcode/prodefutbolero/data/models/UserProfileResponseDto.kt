package com.rockandcode.prodefutbolero.data.models

import com.rockandcode.prodefutbolero.domain.user.models.User

data class UserProfileResponseDto(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val document: String,
    val userName: String,
    val pictureUrl: String,
    val fullName: String,
    val phoneNumber: String,
    val userRoles: String,
    val pictureId: String,
) {
    fun toDomain() =
        User(
            id = userId,
            name = fullName,
            email = userName,
            avatarUrl = pictureUrl,
        )
}
