package com.rockandcode.prodefutbolero.data.models

import com.google.gson.annotations.SerializedName
import com.rockandcode.prodefutbolero.domain.prediction.models.Prediction

class PredictionDto(
    @SerializedName("MatchId")
    val matchId: Int,
    @SerializedName("GroupId")
    val groupId: Int,
    @SerializedName("LocalId")
    val localId: Int,
    @SerializedName("VisitorId")
    val visitorId: Int,
    @SerializedName("StatusMatchId")
    val statusMatchId: Int,
    @SerializedName("Date")
    val date: String,
    @SerializedName("DateId")
    val dateId: Int,
    @SerializedName("LocalName")
    val localName: String,
    // @SerializedName("LocalPictureId")
    // val localPictureId: Int,
    @SerializedName("VisitorName")
    val visitorName: String,
    // @SerializedName("VisitorPictureId")
    // val visitorPictureId: Int,
    @SerializedName("GroupName")
    val groupName: String,
    @SerializedName("TournamentId")
    val tournamentId: Int,
    // @SerializedName("TournamentName")
    // val tournamentName: String,
    @SerializedName("DateName")
    val dateName: String,
    @SerializedName("LocalGoals")
    val localGoals: Int?,
    @SerializedName("VisitorGoals")
    val visitorGoals: Int?,
    // @SerializedName("PredictionId")
    // val predictionId: Int?,
    @SerializedName("UserId")
    val userId: Int?,
    @SerializedName("LocalPictureUrl")
    val localPictureUrl: String,
    @SerializedName("VisitorPictureUrl")
    val visitorPictureUrl: String,
) {
    fun toDomain(): Prediction =
        Prediction(
            matchId = matchId,
            groupId = groupId,
            localId = localId,
            visitorId = visitorId,
            statusMatchId = statusMatchId,
            date = date,
            dateId = dateId,
            localName = localName,
            // localPictureId = localPictureId,
            visitorName = visitorName,
            // visitorPictureId = visitorPictureId,
            groupName = groupName,
            tournamentId = tournamentId,
            // tournamentName = tournamentName,
            dateName = dateName,
            localGoals = localGoals,
            visitorGoals = visitorGoals,
            // predictionId = predictionId,
            userId = userId,
            localPictureUrl = localPictureUrl,
            visitorPictureUrl = visitorPictureUrl,
        )
}
