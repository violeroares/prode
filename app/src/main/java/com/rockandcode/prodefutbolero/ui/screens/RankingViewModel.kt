package com.rockandcode.prodefutbolero.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rockandcode.prodefutbolero.domain.prediction.models.Ranking
import com.rockandcode.prodefutbolero.domain.prediction.models.RankingFilter
import com.rockandcode.prodefutbolero.domain.prediction.repository.IPredictionRepository
import com.rockandcode.prodefutbolero.utils.AppConstants
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
    data object Loading : RankingUiState

    data class Success(
        val rankings: List<Ranking>,
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

    data object PopBackStack : RankingUiEvent()
}

@HiltViewModel
class RankingViewModel
    @Inject
    constructor(
        private val predictionRepository: IPredictionRepository,
    ) : ViewModel() {
        val pageSize = AppConstants.PAGE_SIZE

        private val _screenState = MutableStateFlow(RankingScreenState())
        val screenState: StateFlow<RankingScreenState> = _screenState.asStateFlow()

        private val _eventFlow = MutableSharedFlow<RankingUiEvent>()
        val eventFlow: SharedFlow<RankingUiEvent> = _eventFlow.asSharedFlow()

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
                getRanking(userName = searchQuery, dateId = selectedDateId)
            }
        }

        fun getRanking(
            userName: String? = null,
            dateId: Int? = selectedDateId,
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
                    val filter =
                        RankingFilter(
                            tournamentId = tournamentId?.toString(),
                            userName = userName,
                            dateId = dateId?.toString(),
                        )
                    val offset = (currentPage - 1) * pageSize

                    val result =
                        predictionRepository.getRankingToPage(
                            filter = filter,
                            pageIndex = offset,
                            pageSize = pageSize,
                            sort = "",
                        )

                    totalPages = result.totalPages

                    _screenState.update {
                        it.copy(
                            uiState =
                                RankingUiState.Success(
                                    rankings = result.result,
                                    currentPage = currentPage,
                                    totalPages = totalPages,
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
            userName: String? = null,
            dateId: Int? = selectedDateId,
        ) {
            val state = _screenState.value.uiState
            if (state !is RankingUiState.Success) return
            if (currentPage > totalPages || isPaginating) return
            if (dateId != selectedDateId) return

            viewModelScope.launch {
                isPaginating = true
                try {
                    currentPage++
                    val filter =
                        RankingFilter(
                            tournamentId = tournamentId?.toString(),
                            userName = userName,
                            dateId = dateId?.toString(),
                        )
                    val nextPage = currentPage + 1
                    val offset = (nextPage - 1) * pageSize

                    val result =
                        predictionRepository.getRankingToPage(
                            filter = filter,
                            pageIndex = offset,
                            pageSize = pageSize,
                            sort = "",
                        )

                    currentPage = nextPage
                    totalPages = result.totalPages

                    _screenState.update {
                        it.copy(
                            uiState =
                                RankingUiState.Success(
                                    rankings = state.rankings + result.result,
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

//        fun onBackPressed() {
//            viewModelScope.launch {
//                _eventFlow.emit(RankingUiEvent.PopBackStack)
//            }
//        }
    }
