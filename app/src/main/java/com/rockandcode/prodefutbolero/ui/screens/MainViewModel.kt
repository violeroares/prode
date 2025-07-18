package com.rockandcode.prodefutbolero.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rockandcode.prodefutbolero.domain.prediction.models.PredictionSummary
import com.rockandcode.prodefutbolero.domain.prediction.repository.IPredictionRepository
import com.rockandcode.prodefutbolero.domain.tournament.models.MatchDate
import com.rockandcode.prodefutbolero.domain.tournament.models.Tournament
import com.rockandcode.prodefutbolero.domain.tournament.repository.ITournamentRepository
import com.rockandcode.prodefutbolero.domain.user.models.User
import com.rockandcode.prodefutbolero.domain.user.usecases.ClearSessionUseCase
import com.rockandcode.prodefutbolero.domain.user.usecases.GetUserSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val getUserSessionUseCase: GetUserSessionUseCase,
        private val clearSessionUseCase: ClearSessionUseCase,
        private val tournamentRepository: ITournamentRepository,
        private val predictionRepository: IPredictionRepository,
    ) : ViewModel() {
        private val _user = MutableStateFlow<User?>(null)
        val user: StateFlow<User?> = _user

        val isAuthenticated: StateFlow<Boolean> =
            _user
                .map { it != null }
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

        init {
            viewModelScope.launch {
                getUserSessionUseCase().collect {
                    _user.value = it
                }
            }
        }

        private val _selectedTournament = MutableStateFlow<Tournament?>(null)
        val selectedTournament: StateFlow<Tournament?> = _selectedTournament

        private val _dates = MutableStateFlow<List<MatchDate>>(emptyList())
        val dates: StateFlow<List<MatchDate>> = _dates

        private val _prediccionesIncompletas = MutableStateFlow<Int>(0)
        val prediccionesIncompletas: StateFlow<Int> = _prediccionesIncompletas

        private val _predictionSummary = MutableStateFlow<PredictionSummary?>(null)
        val predictionSummary: StateFlow<PredictionSummary?> = _predictionSummary

        fun selectTournament(tournament: Tournament) {
            _selectedTournament.value = tournament
            viewModelScope.launch {
                _dates.value = tournamentRepository.getDates(tournament.id.toString())
                _prediccionesIncompletas.value =
                    predictionRepository.getPrediccionesIncompletas(
                        userId = _user.value?.id?.toInt() ?: 0,
                        tournamentId = _selectedTournament.value?.id ?: 0,
                        dateId = _dates.value.find { it.active == true }?.id ?: 0,
                    )
                _predictionSummary.value =
                    predictionRepository.getPredictionSummary(
                        userId = _user.value?.id?.toString() ?: "0",
                        dateId =
                            _dates.value
                                .find { it.active == true }
                                ?.id
                                .toString(),
                    )
            }
        }

        fun logout() {
            viewModelScope.launch {
                clearSessionUseCase()
            }
        }
    }
