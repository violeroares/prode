package com.rockandcode.prodefutbolero.data.datasources.network

import com.rockandcode.prodefutbolero.data.models.AverageDto
import com.rockandcode.prodefutbolero.data.models.ChangePasswordRequest
import com.rockandcode.prodefutbolero.data.models.HitDto
import com.rockandcode.prodefutbolero.data.models.HitRequest
import com.rockandcode.prodefutbolero.data.models.LoginRequest
import com.rockandcode.prodefutbolero.data.models.LoginResponse
import com.rockandcode.prodefutbolero.data.models.MatchDateDto
import com.rockandcode.prodefutbolero.data.models.MatchDto
import com.rockandcode.prodefutbolero.data.models.MatchRequest
import com.rockandcode.prodefutbolero.data.models.PageResult
import com.rockandcode.prodefutbolero.data.models.Pagination
import com.rockandcode.prodefutbolero.data.models.PredictionDto
import com.rockandcode.prodefutbolero.data.models.PredictionSummaryDto
import com.rockandcode.prodefutbolero.data.models.RankingDto
import com.rockandcode.prodefutbolero.data.models.RankingRequest
import com.rockandcode.prodefutbolero.data.models.TournamentResponseDto
import com.rockandcode.prodefutbolero.data.models.UserProfileResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): Response<LoginResponse>

    @POST("api/auth/ChangePassword")
    suspend fun changePassword(
        @Body changePasswordRequest: ChangePasswordRequest,
    ): Response<Boolean>

    @GET("api/auth/currentAuthUser")
    suspend fun getUserProfile(): Response<UserProfileResponseDto>

    @GET("api/tournaments/GetTournamentsForHeader")
    suspend fun getTournaments(): List<TournamentResponseDto>

    @GET("api/Dates/GetDatesKV")
    suspend fun getDates(
        @Query("tournamentId") tournamentId: String,
    ): List<MatchDateDto>

    @POST("api/predictions/GetRankingToPage")
    suspend fun getRankingToPage(
        @Body input: Pagination<RankingRequest>,
    ): Response<PageResult<RankingDto>>

    @POST("api/predictions/GetHitsToPage")
    suspend fun getHitsToPage(
        @Body input: Pagination<HitRequest>,
    ): Response<PageResult<HitDto>>

    @POST("api/matches/GetMatchesToPageV3")
    suspend fun getMatchesToPage(
        @Body input: Pagination<MatchRequest>,
    ): Response<PageResult<MatchDto>>

    @GET("api/Predictions/GetAverageByUser")
    suspend fun getAverageByUser(
        @Query("tournamentId") tournamentId: Int?,
    ): List<AverageDto>

    @GET("api/Predictions/GetPredictionsIncompletasApp")
    suspend fun getPrediccionesIncompletas(
        @Query("userId") userId: Int? = null,
        @Query("tournamentId") tournamentId: Int,
        @Query("dateId") dateId: Int? = null,
    ): Response<Int>

    @GET("api/Predictions/GetPredictionSummary")
    suspend fun getPredictionSummary(
        @Query("userId") userId: String,
        @Query("dateId") dateId: String,
    ): Response<PredictionSummaryDto>

    @GET("api/Predictions/GetPredictionsApp")
    suspend fun getPredictionsApp(
        @Query("userId") userId: String?,
        @Query("tournamentId") tournamentId: String?,
        @Query("dateId") dateId: String?,
    ): List<PredictionDto>
}
