package com.rockandcode.prodefutbolero.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rockandcode.prodefutbolero.domain.tournament.models.TournamentHome
import com.rockandcode.prodefutbolero.domain.tournament.usecases.GetTournamentHomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeUiState {
    object Loading : HomeUiState

    data class Success(
        val data: TournamentHome,
    ) : HomeUiState

    data class Error(
        val message: String,
    ) : HomeUiState
}

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val getTournamentHomeUseCase: GetTournamentHomeUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
        val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

        fun loadTournamentHome(tournamentId: Int) {
            viewModelScope.launch {
                _uiState.value = HomeUiState.Loading
                try {
                    val tournamentHome = getTournamentHomeUseCase(tournamentId)
                    Log.d("HomeViewModel", "$tournamentHome")
                    _uiState.value = HomeUiState.Success(tournamentHome)
                } catch (e: Exception) {
                    _uiState.value = HomeUiState.Error(e.message ?: "Error desconocido")
                }
            }
        }
    }
