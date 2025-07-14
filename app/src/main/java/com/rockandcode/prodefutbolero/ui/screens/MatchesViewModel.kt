package com.rockandcode.prodefutbolero.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rockandcode.prodefutbolero.domain.tournament.models.Match
import com.rockandcode.prodefutbolero.domain.tournament.models.MatchDate
import com.rockandcode.prodefutbolero.domain.tournament.repository.ITournamentRepository
import com.rockandcode.prodefutbolero.domain.tournament.usecases.GetMatchesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MatchesUiState {
    object Loading : MatchesUiState

    data class Success(
        val matches: List<Match>,
        val currentPage: Int,
        val totalPages: Int,
    ) : MatchesUiState

    data class Error(
        val message: String,
    ) : MatchesUiState
}

data class MatchesScreenState(
    val uiState: MatchesUiState = MatchesUiState.Loading,
    val dates: List<MatchDate> = emptyList(),
    val isRefreshing: Boolean = false,
)

sealed class UiEvent {
    data class ShowSnackbar(
        val message: String,
    ) : UiEvent()

    data class Navigate(
        val route: String,
    ) : UiEvent()

    object PopBackStack : UiEvent()
}

@HiltViewModel
class MatchesViewModel
    @Inject
    constructor(
        private val getMatchesUseCase: GetMatchesUseCase,
        private val tournamentRepository: ITournamentRepository,
    ) : ViewModel() {
        private val _screenState = MutableStateFlow(MatchesScreenState())
        val screenState: StateFlow<MatchesScreenState> = _screenState.asStateFlow()

        private val _eventFlow = MutableSharedFlow<UiEvent>()
        val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

        var selectedDateId by mutableStateOf<Int?>(null)
        var searchQuery by mutableStateOf("")
            private set

        fun loadDates(tournamentId: Int) {
            viewModelScope.launch {
                val dates = tournamentRepository.getDates(tournamentId)
                _screenState.update { it.copy(dates = dates) }
            }
        }

        fun onSearchQueryChanged(
            newQuery: String,
            tournamentId: Int?,
        ) {
            searchQuery = newQuery
            if (searchQuery.length >= 3 || searchQuery.isEmpty()) {
                refresh(tournamentId, teamName = searchQuery, dateId = selectedDateId)
            }
        }

        fun refresh(
            tournamentId: Int?,
            teamName: String? = null,
            dateId: Int? = null,
            isPullToRefresh: Boolean = false,
        ) {
            viewModelScope.launch {
                if (!isPullToRefresh) {
                    _screenState.update { it.copy(uiState = MatchesUiState.Loading) }
                } else {
                    _screenState.update { it.copy(isRefreshing = true) }
                }

                try {
                    val result =
                        getMatchesUseCase(
                            tournamentId = tournamentId,
                            page = 1,
                            teamName = teamName,
                            dateId = dateId,
                        )
                    _screenState.update {
                        it.copy(
                            uiState =
                                MatchesUiState.Success(
                                    matches = result.matches,
                                    currentPage = 2,
                                    totalPages = result.totalPages,
                                ),
                            isRefreshing = false,
                        )
                    }
                } catch (e: Exception) {
                    _screenState.update {
                        it.copy(
                            uiState = MatchesUiState.Error(e.message ?: "Error desconocido"),
                            isRefreshing = false,
                        )
                    }
                    _eventFlow.emit(UiEvent.ShowSnackbar(e.message ?: "Error desconocido"))
                }
            }
        }

        fun loadNextPage(
            tournamentId: Int?,
            teamName: String? = null,
            dateId: Int? = null,
        ) {
            val current = _screenState.value
            val state = current.uiState
            if (state !is MatchesUiState.Success) return
            if (state.currentPage > state.totalPages) return

            viewModelScope.launch {
                try {
                    val result =
                        getMatchesUseCase(
                            tournamentId = tournamentId,
                            page = state.currentPage,
                            teamName = teamName,
                            dateId = dateId,
                        )
                    _screenState.update {
                        it.copy(
                            uiState =
                                MatchesUiState.Success(
                                    matches = state.matches + result.matches,
                                    currentPage = state.currentPage + 1,
                                    totalPages = result.totalPages,
                                ),
                        )
                    }
                } catch (e: Exception) {
                    _eventFlow.emit(UiEvent.ShowSnackbar(e.message ?: "Error al cargar más resultados"))
                }
            }
        }

        fun onMatchClick(match: Match) {
            viewModelScope.launch {
                _eventFlow.emit(UiEvent.Navigate("matchDetail/${match.matchId}"))
            }
        }

        fun onBackPressed() {
            viewModelScope.launch {
                _eventFlow.emit(UiEvent.PopBackStack)
            }
        }
    }
