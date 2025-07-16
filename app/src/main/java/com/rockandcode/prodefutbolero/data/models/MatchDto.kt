package com.rockandcode.prodefutbolero.data.models

import com.google.gson.annotations.SerializedName
import com.rockandcode.prodefutbolero.domain.match.models.Match

data class MatchDto(
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
    @SerializedName("LocalGoals")
    val localGoals: Int?,
    @SerializedName("VisitorGoals")
    val visitorGoals: Int?,
    @SerializedName("Date")
    val date: String,
    @SerializedName("DateId")
    val dateId: Int,
    @SerializedName("LocalName")
    val localName: String,
    @SerializedName("VisitorName")
    val visitorName: String,
    @SerializedName("DisplayOrder")
    val displayOrder: Int,
    @SerializedName("GroupName")
    val groupName: String,
    @SerializedName("DateName")
    val dateName: String,
    @SerializedName("LocalPictureId")
    val localPictureId: Int,
    @SerializedName("VisitorPictureId")
    val visitorPictureId: Int,
    @SerializedName("Predictions")
    val predictions: Int,
    @SerializedName("LocalPictureUrl")
    val localPictureUrl: String,
    @SerializedName("VisitorPictureUrl")
    val visitorPictureUrl: String,
) {
    fun toDomain() =
        Match(
            matchId = this.matchId,
            groupId = this.groupId,
            localId = this.localId,
            visitorId = this.visitorId,
            statusMatchId = this.statusMatchId,
            localGoals = this.localGoals,
            visitorGoals = this.visitorGoals,
            date = this.date,
            dateId = this.dateId,
            localName = this.localName,
            visitorName = this.visitorName,
            displayOrder = this.displayOrder,
            groupName = this.groupName,
            dateName = this.dateName,
            localPictureId = this.localPictureId,
            visitorPictureId = this.visitorPictureId,
            predictions = this.predictions,
            localPictureUrl = this.localPictureUrl,
            visitorPictureUrl = this.visitorPictureUrl,
        )
}
