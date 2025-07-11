package com.rockandcode.prodefutbolero.data.datasources.network

import com.rockandcode.prodefutbolero.data.models.LoginRequest
import com.rockandcode.prodefutbolero.data.models.LoginResponse
import com.rockandcode.prodefutbolero.data.models.TournamentResponseDto
import com.rockandcode.prodefutbolero.data.models.UserProfileResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): Response<LoginResponse>

    @GET("api/auth/currentAuthUser")
    suspend fun getUserProfile(): Response<UserProfileResponseDto>

    @GET("api/Tournaments/GetTournamentsForHeader")
    suspend fun getTournaments(): List<TournamentResponseDto>

//    @GET("api/Matches/GetClosedMatchesApp")
//    suspend fun getMatches(
//        @Query("tournamentId") tournamentId: String,
//        @Query("dateId") dateId: String,
//    ): List<MatchResponseDto>
}
