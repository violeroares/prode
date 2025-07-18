package com.rockandcode.prodefutbolero.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rockandcode.prodefutbolero.domain.prediction.models.Ranking
import com.rockandcode.prodefutbolero.domain.prediction.models.RankingFilter
import com.rockandcode.prodefutbolero.domain.prediction.repository.IPredictionRepository
import com.rockandcode.prodefutbolero.domain.tournament.models.AverageByDate
import com.rockandcode.prodefutbolero.domain.tournament.models.TournamentHome
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val data: TournamentHome?,
        val myRanking: Ranking?,
        val topRanking: Ranking?,
        val averageByDate: List<AverageByDate>,
    ) : HomeUiState

    data class Error(
        val message: String,
    ) : HomeUiState
}

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val predictionsRepo: IPredictionRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
        val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

        fun loadTournamentHome(
            tournamentId: Int?,
            userId: String?,
        ) {
            viewModelScope.launch {
                _uiState.value = HomeUiState.Loading
                try {
                    // val homeDeferred = async { repo.getTournamentHome(tournamentId ?: 0) }
                    Log.d("HomeViewModel", "loadTournamentHome")

                    val averageDeferred =
                        async {
                            predictionsRepo.getAverageByUser(tournamentId = tournamentId)
                        }

                    val myRankingDeferred =
                        async {
                            val filter = RankingFilter(userId = userId, tournamentId = tournamentId?.toString())
                            predictionsRepo.getRankingToPage(filter, pageIndex = 0, pageSize = 1, sort = "").result.firstOrNull()
                        }

//                    val topRankingDeferred =
//                        async {
//                            val filter = RankingFilter(posicion = "1", tournamentId = tournamentId.toString())
//                            predictionsRepo.getRankingToPage(filter, pageIndex = 1, pageSize = 1, sort = "").result.firstOrNull()
//                        }

                    // val home = homeDeferred.await()
                    val myRanking = myRankingDeferred.await()
                    val averageByDate = averageDeferred.await()
                    // val topRanking = topRankingDeferred.await()
                    Log.d("HomeViewModel", "MyRanking: $myRanking")
                    Log.d("HomeViewModel", "Average: $averageByDate")
                    _uiState.value =
                        HomeUiState.Success(
                            data = null,
                            myRanking = myRanking,
                            topRanking = null,
                            averageByDate = averageByDate,
                        )
                } catch (e: Exception) {
                    _uiState.value = HomeUiState.Error(e.message ?: "Error desconocido")
                }
            }
        }
    }
