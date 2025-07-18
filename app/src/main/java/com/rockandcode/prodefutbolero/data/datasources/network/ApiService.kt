package com.rockandcode.prodefutbolero.data.datasources.network

import com.rockandcode.prodefutbolero.data.models.AverageDto
import com.rockandcode.prodefutbolero.data.models.HitDto
import com.rockandcode.prodefutbolero.data.models.HitRequest
import com.rockandcode.prodefutbolero.data.models.LoginRequest
import com.rockandcode.prodefutbolero.data.models.LoginResponse
import com.rockandcode.prodefutbolero.data.models.MatchDateDto
import com.rockandcode.prodefutbolero.data.models.MatchDto
import com.rockandcode.prodefutbolero.data.models.MatchRequest
import com.rockandcode.prodefutbolero.data.models.PageResult
import com.rockandcode.prodefutbolero.data.models.PaginatedMatchesDto
import com.rockandcode.prodefutbolero.data.models.PaginatedRankingDto
import com.rockandcode.prodefutbolero.data.models.Pagination
import com.rockandcode.prodefutbolero.data.models.PredictionSummaryDto
import com.rockandcode.prodefutbolero.data.models.RankingDto
import com.rockandcode.prodefutbolero.data.models.RankingRequest
import com.rockandcode.prodefutbolero.data.models.TournamentHomeResponseDto
import com.rockandcode.prodefutbolero.data.models.TournamentResponseDto
import com.rockandcode.prodefutbolero.data.models.UserProfileResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): Response<LoginResponse>

    @GET("api/auth/currentAuthUser")
    suspend fun getUserProfile(): Response<UserProfileResponseDto>

//    @GET("api/MobileV3/GetTournaments")
//    suspend fun getTournaments(): List<TournamentResponseDto>

    @GET("api/tournaments/GetTournamentsForHeader")
    suspend fun getTournaments(): List<TournamentResponseDto>

    @GET("api/MobileV3/{id}/home")
    suspend fun getTournamentHome(
        @Path("id") tournamentId: Int,
    ): Response<TournamentHomeResponseDto>

    @GET("api/MobileV3/GetMatchesAsync")
    suspend fun getMatches(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("tournamentId") tournamentId: Int?,
        @Query("teamName") teamName: String?,
        @Query("dateId") dateId: Int?,
    ): PaginatedMatchesDto

    @GET("api/Dates/GetDatesKV")
    suspend fun getDates(
        @Query("tournamentId") tournamentId: String,
    ): List<MatchDateDto>

    @GET("api/MobileV3/GetRankingAsync")
    suspend fun getRanking(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("tournamentId") tournamentId: Int,
        @Query("dateId") dateId: Int? = null,
        @Query("firstname") firstname: String? = null,
        @Query("lastname") lastname: String? = null,
        @Query("username") username: String? = null,
        @Query("posicion") posicion: Int? = null,
        @Query("fechaDesde") fechaDesde: String? = null, // ISO 8601 format: "2025-07-14T00:00:00"
        @Query("fechaHasta") fechaHasta: String? = null,
        @Query("userId") userId: Int? = null,
    ): Response<PaginatedRankingDto>

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
}
