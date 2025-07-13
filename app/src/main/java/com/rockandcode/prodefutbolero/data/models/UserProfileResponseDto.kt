package com.rockandcode.prodefutbolero.data.models

import com.google.gson.annotations.SerializedName
import com.rockandcode.prodefutbolero.domain.user.models.User

data class UserProfileResponseDto(
    @SerializedName("userId")
    val id: String,
    val firstName: String,
    val lastName: String,
    @SerializedName("userName")
    val email: String,
    @SerializedName("pictureUrl")
    val avatarUrl: String,
    val phoneNumber: String,
) {
    fun toDomain() =
        User(
            id = id,
            firstName = firstName,
            lastName = lastName,
            email = email,
            avatarUrl = avatarUrl,
        )
}
