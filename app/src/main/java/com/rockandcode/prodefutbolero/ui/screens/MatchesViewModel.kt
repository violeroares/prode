package com.rockandcode.prodefutbolero.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rockandcode.prodefutbolero.domain.match.models.Match
import com.rockandcode.prodefutbolero.domain.match.models.MatchFilter
import com.rockandcode.prodefutbolero.domain.match.repository.IMatchRepository
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
    data object Loading : MatchesUiState

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
    val isRefreshing: Boolean = false,
)

sealed class MatchesUiEvent {
    data class ShowSnackbar(
        val message: String,
    ) : MatchesUiEvent()

    data class Navigate(
        val route: String,
    ) : MatchesUiEvent()

    data object PopBackStack : MatchesUiEvent()
}

@HiltViewModel
class MatchesViewModel
    @Inject
    constructor(
        private val repo: IMatchRepository,
    ) : ViewModel() {
        private val _screenState = MutableStateFlow(MatchesScreenState())
        val screenState: StateFlow<MatchesScreenState> = _screenState.asStateFlow()

        private val _eventFlow = MutableSharedFlow<MatchesUiEvent>()
        val eventFlow: SharedFlow<MatchesUiEvent> = _eventFlow.asSharedFlow()

        private var tournamentId: Int? = null

        var currentPage = 1
            private set
        internal var totalPages = 1

        var isPaginating = false
            private set

        var selectedDateId by mutableStateOf<Int?>(null)
        var searchQuery by mutableStateOf("")
            private set

        fun setContext(tournamentId: Int?) {
            this.tournamentId = tournamentId
        }

        fun onSearchQueryChanged(newQuery: String) {
            searchQuery = newQuery
            if (searchQuery.length >= 3 || searchQuery.isEmpty()) {
                getMatches(teamName = searchQuery, dateId = selectedDateId)
            }
        }

        fun getMatches(
            teamName: String? = null,
            dateId: Int? = selectedDateId,
            isPullToRefresh: Boolean = false,
        ) {
            viewModelScope.launch {
                if (isPullToRefresh) {
                    _screenState.update { it.copy(isRefreshing = true) }
                } else {
                    _screenState.update { it.copy(uiState = MatchesUiState.Loading) }
                }

                currentPage = 1
                totalPages = 1

                try {
                    val filter =
                        MatchFilter(
                            tournamentId = tournamentId?.toString(),
                            teamName = teamName,
                            dateId = dateId?.toString(),
                        )

                    val result =
                        repo.getMatchesToPage(
                            filter = filter,
                            pageIndex = currentPage,
                            pageSize = 20,
                            sort = "",
                        )

                    currentPage = result.pageIndex + 1
                    totalPages = result.totalPages

                    _screenState.update {
                        it.copy(
                            uiState =
                                MatchesUiState.Success(
                                    matches = result.result,
                                    currentPage = result.pageIndex,
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
                    _eventFlow.emit(MatchesUiEvent.ShowSnackbar(e.message ?: "Error desconocido"))
                }
            }
        }

        fun loadNextPage(
            teamName: String? = null,
            dateId: Int? = selectedDateId,
        ) {
            val state = _screenState.value.uiState
            if (state !is MatchesUiState.Success) return
            if (currentPage > totalPages || isPaginating) return
            if (dateId != selectedDateId) return

            viewModelScope.launch {
                isPaginating = true
                try {
                    val filter =
                        MatchFilter(
                            tournamentId = tournamentId?.toString(),
                            teamName = teamName,
                            dateId = dateId?.toString(),
                        )

                    val result =
                        repo.getMatchesToPage(
                            filter = filter,
                            pageIndex = currentPage,
                            pageSize = 20,
                            sort = "",
                        )

                    currentPage++
                    totalPages = result.totalPages

                    _screenState.update {
                        it.copy(
                            uiState =
                                MatchesUiState.Success(
                                    matches = state.matches + result.result,
                                    currentPage = currentPage,
                                    totalPages = totalPages,
                                ),
                        )
                    }
                } catch (e: Exception) {
                    _eventFlow.emit(MatchesUiEvent.ShowSnackbar(e.message ?: "Error desconocido"))
                } finally {
                    isPaginating = false
                }
            }
        }

        fun onMatchClick(match: Match) {
            viewModelScope.launch {
                _eventFlow.emit(MatchesUiEvent.Navigate("matchDetail/${match.matchId}"))
            }
        }

        fun onBackPressed() {
            viewModelScope.launch {
                _eventFlow.emit(MatchesUiEvent.PopBackStack)
            }
        }
    }
