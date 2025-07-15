package com.rockandcode.prodefutbolero.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rockandcode.prodefutbolero.domain.tournament.models.RankingUser
import com.rockandcode.prodefutbolero.domain.tournament.repository.ITournamentRepository
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

sealed interface RankingUiState {
    object Loading : RankingUiState

    data class Success(
        val rankings: List<RankingUser>,
        val currentPage: Int,
        val totalPages: Int,
    ) : RankingUiState

    data class Error(
        val message: String,
    ) : RankingUiState
}

data class RankingScreenState(
    val uiState: RankingUiState = RankingUiState.Loading,
    val isRefreshing: Boolean = false,
)

sealed class RankingUiEvent {
    data class ShowSnackbar(
        val message: String,
    ) : RankingUiEvent()

    data class Navigate(
        val route: String,
    ) : RankingUiEvent()

    object PopBackStack : RankingUiEvent()
}

@HiltViewModel
class RankingViewModel
    @Inject
    constructor(
        private val tournamentRepository: ITournamentRepository,
    ) : ViewModel() {
        private val _screenState = MutableStateFlow(RankingScreenState())
        val screenState: StateFlow<RankingScreenState> = _screenState.asStateFlow()

        private val _eventFlow = MutableSharedFlow<RankingUiEvent>()
        val eventFlow: SharedFlow<RankingUiEvent> = _eventFlow.asSharedFlow()

        var currentPage = 1
            private set
        internal var totalPages = 1

//        var isLoading = false
//            private set
        var isPaginating = false
            private set

        fun getRanking(
            tournamentId: Int,
            dateId: Int? = null,
            isPullToRefresh: Boolean = false,
        ) {
            viewModelScope.launch {
                if (isPullToRefresh) {
                    _screenState.update { it.copy(isRefreshing = true) }
                } else {
                    _screenState.update { it.copy(uiState = RankingUiState.Loading) }
                }

                currentPage = 1
                totalPages = 1

                try {
                    val result =
                        tournamentRepository.getRanking(
                            tournamentId = tournamentId,
                            page = currentPage,
                            dateId = dateId,
                        )
                    currentPage = result.currentPage + 1
                    totalPages = result.totalPages

                    _screenState.update {
                        it.copy(
                            uiState =
                                RankingUiState.Success(
                                    rankings = result.rankings,
                                    currentPage = result.currentPage,
                                    totalPages = result.totalPages,
                                ),
                            isRefreshing = false,
                        )
                    }
                } catch (e: Exception) {
                    _screenState.update {
                        it.copy(
                            uiState = RankingUiState.Error(e.message ?: "Error desconocido"),
                            isRefreshing = false,
                        )
                    }
                    _eventFlow.emit(RankingUiEvent.ShowSnackbar(e.message ?: "Error desconocido"))
                }
            }
        }

        fun loadNextPage(
            tournamentId: Int,
            dateId: Int? = null,
        ) {
            val state = _screenState.value.uiState
            if (state !is RankingUiState.Success) return
            if (currentPage > totalPages || isPaginating) return

            viewModelScope.launch {
                isPaginating = true
                try {
                    val result =
                        tournamentRepository.getRanking(
                            tournamentId = tournamentId,
                            page = currentPage,
                            dateId = dateId,
                        )

                    currentPage++
                    totalPages = result.totalPages

                    _screenState.update {
                        it.copy(
                            uiState =
                                RankingUiState.Success(
                                    rankings = state.rankings + result.rankings,
                                    currentPage = currentPage,
                                    totalPages = totalPages,
                                ),
                        )
                    }
                } catch (e: Exception) {
                    _eventFlow.emit(RankingUiEvent.ShowSnackbar(e.message ?: "Error desconocido"))
                } finally {
                    isPaginating = false
                }
            }
        }

        fun onBackPressed() {
            viewModelScope.launch {
                _eventFlow.emit(RankingUiEvent.PopBackStack)
            }
        }
    }
