package com.rockandcode.prodefutbolero.data.models

import com.google.gson.annotations.SerializedName
import com.rockandcode.prodefutbolero.domain.prediction.models.Hit

class HitDto(
    @SerializedName("PredictionId")
    val predictionId: Int,
    @SerializedName("MatchId")
    val matchId: Int,
    @SerializedName("MyLocalGoals")
    val myLocalGoals: Int,
    @SerializedName("MyVisitorGoals")
    val myVisitorGoals: Int,
    @SerializedName("UserId")
    val userId: Int,
    @SerializedName("Points")
    val points: Int,
    @SerializedName("LocalId")
    val localId: Int,
    @SerializedName("VisitorId")
    val visitorId: Int,
    @SerializedName("StatusMatchId")
    val statusMatchId: Int,
    @SerializedName("Username")
    val userName: String,
    @SerializedName("FirstName")
    val firstName: String,
    @SerializedName("LastName")
    val lastName: String,
    @SerializedName("PictureId")
    val pictureId: String,
    @SerializedName("LocalName")
    val localName: String,
    @SerializedName("LocalPictureId")
    val localPictureId: Int,
    @SerializedName("VisitorName")
    val visitorName: String,
    @SerializedName("VisitorPictureId")
    val visitorPictureId: Int,
    @SerializedName("RealLocalGoals")
    val realLocalGoals: Int,
    @SerializedName("RealVisitorGoals")
    val realVisitorGoals: Int,
    @SerializedName("Date")
    val date: String,
    @SerializedName("DateId")
    val dateId: Int,
    @SerializedName("DateName")
    val dateName: String,
    @SerializedName("GroupName")
    val groupName: String,
    @SerializedName("UserPictureUrl")
    val userPictureUrl: String,
    @SerializedName("LocalPictureUrl")
    val localPictureUrl: String,
    @SerializedName("VisitorPictureUrl")
    val visitorPictureUrl: String,
) {
    fun toDomain(): Hit =
        Hit(
            predictionId = predictionId,
            matchId = matchId,
            myLocalGoals = myLocalGoals,
            myVisitorGoals = myVisitorGoals,
            userId = userId,
            points = points,
            localId = localId,
            visitorId = visitorId,
            statusMatchId = statusMatchId,
            userName = userName,
            firstName = firstName,
            lastName = lastName,
            pictureId = pictureId,
            localName = localName,
            localPictureId = localPictureId,
            visitorName = visitorName,
            visitorPictureId = visitorPictureId,
            realLocalGoals = realLocalGoals,
            realVisitorGoals = realVisitorGoals,
            date = date,
            dateId = dateId,
            dateName = dateName,
            groupName = groupName,
            userPictureUrl = userPictureUrl,
            localPictureUrl = localPictureUrl,
            visitorPictureUrl = visitorPictureUrl,
        )
}
