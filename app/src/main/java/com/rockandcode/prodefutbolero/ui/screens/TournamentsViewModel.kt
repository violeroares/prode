package com.rockandcode.prodefutbolero.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rockandcode.prodefutbolero.domain.tournament.models.Tournament
import com.rockandcode.prodefutbolero.domain.tournament.repository.ITournamentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class TournamentsUiState {
    object Loading : TournamentsUiState()

    data class Success(
        val tournaments: List<Tournament>,
    ) : TournamentsUiState()

    data class Error(
        val message: String,
    ) : TournamentsUiState()
}

@HiltViewModel
class TournamentsViewModel
    @Inject
    constructor(
        private val repo: ITournamentRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<TournamentsUiState>(TournamentsUiState.Loading)
        val uiState: StateFlow<TournamentsUiState> = _uiState.asStateFlow()

        init {
            loadTournaments()
        }

        fun loadTournaments() {
            viewModelScope.launch {
                repo
                    .getTournaments()
                    .catch { e ->
                        _uiState.value = TournamentsUiState.Error(e.message ?: "Error desconocido")
                    }.collect { tournaments ->
                        _uiState.value = TournamentsUiState.Success(tournaments)
                    }
            }
        }
    }
