package com.rockandcode.prodefutbolero.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rockandcode.prodefutbolero.domain.prediction.models.Hit
import com.rockandcode.prodefutbolero.domain.prediction.models.HitFilter
import com.rockandcode.prodefutbolero.domain.prediction.repository.IPredictionRepository
import com.rockandcode.prodefutbolero.domain.tournament.models.MatchDate
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

sealed interface HitsUiState {
    data object Loading : HitsUiState

    data class Success(
        val hits: List<Hit>,
        val currentPage: Int,
        val totalPages: Int,
    ) : HitsUiState

    data class Error(
        val message: String,
    ) : HitsUiState
}

data class HitsScreenState(
    val uiState: HitsUiState = HitsUiState.Loading,
    val dates: List<MatchDate> = emptyList(),
    val isRefreshing: Boolean = false,
)

sealed class HitsUiEvent {
    data class ShowSnackbar(
        val message: String,
    ) : HitsUiEvent()

    data class Navigate(
        val route: String,
    ) : HitsUiEvent()

    data object PopBackStack : HitsUiEvent()
}

@HiltViewModel
class MyHitsViewModel
    @Inject
    constructor(
        private val predictionRepository: IPredictionRepository,
    ) : ViewModel() {
        val pageSize = AppConstants.PAGE_SIZE

        private val _screenState = MutableStateFlow(HitsScreenState())
        val screenState: StateFlow<HitsScreenState> = _screenState.asStateFlow()

        private val _eventFlow = MutableSharedFlow<HitsUiEvent>()
        val eventFlow: SharedFlow<HitsUiEvent> = _eventFlow.asSharedFlow()

        private var userId: String? = null
        private var tournamentId: Int? = null

        var currentPage = 1
            private set
        internal var totalPages = 1

        var isPaginating = false
            private set

        var selectedDateId by mutableStateOf<Int?>(null)
        var searchQuery by mutableStateOf("")
            private set

        fun onSearchQueryChanged(newQuery: String) {
            searchQuery = newQuery
            if (searchQuery.length >= 3 || searchQuery.isEmpty()) {
                getHits(teamName = searchQuery, dateId = selectedDateId)
            }
        }

        fun setContext(
            userId: String?,
            tournamentId: Int?,
        ) {
            this.userId = userId
            this.tournamentId = tournamentId
        }

        fun getHits(
            teamName: String? = null,
            dateId: Int? = selectedDateId,
            isPullToRefresh: Boolean = false,
        ) {
            viewModelScope.launch {
                if (isPullToRefresh) {
                    _screenState.update { it.copy(isRefreshing = true) }
                } else {
                    _screenState.update { it.copy(uiState = HitsUiState.Loading) }
                }

                currentPage = 1
                totalPages = 1

                try {
                    val filter =
                        HitFilter(
                            userId = userId,
                            tournamentId = tournamentId?.toString(),
                            teamName = teamName,
                            dateId = dateId?.toString(),
                        )

                    val offset = (currentPage - 1) * pageSize

                    val result =
                        predictionRepository.getHitsToPage(
                            filter = filter,
                            pageIndex = offset,
                            pageSize = pageSize,
                            sort = "",
                        )

                    totalPages = result.totalPages

                    _screenState.update {
                        it.copy(
                            uiState =
                                HitsUiState.Success(
                                    hits = result.result,
                                    currentPage = currentPage,
                                    totalPages = totalPages,
                                ),
                            isRefreshing = false,
                        )
                    }
                } catch (e: Exception) {
                    _screenState.update {
                        it.copy(
                            uiState = HitsUiState.Error(e.message ?: "Error desconocido"),
                            isRefreshing = false,
                        )
                    }
                    _eventFlow.emit(HitsUiEvent.ShowSnackbar(e.message ?: "Error desconocido"))
                }
            }
        }

        fun loadNextPage(
            teamName: String? = null,
            dateId: Int? = selectedDateId,
        ) {
            val state = _screenState.value.uiState
            if (state !is HitsUiState.Success) return
            if (currentPage > totalPages || isPaginating) return
            if (dateId != selectedDateId) return

            viewModelScope.launch {
                isPaginating = true
                try {
                    val filter =
                        HitFilter(
                            userId = userId,
                            tournamentId = tournamentId?.toString(),
                            teamName = teamName,
                            dateId = dateId?.toString(),
                        )

                    val nextPage = currentPage + 1
                    val offset = (nextPage - 1) * pageSize

                    val result =
                        predictionRepository.getHitsToPage(
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
                                HitsUiState.Success(
                                    hits = state.hits + result.result,
                                    currentPage = currentPage,
                                    totalPages = totalPages,
                                ),
                        )
                    }
                } catch (e: Exception) {
                    _eventFlow.emit(HitsUiEvent.ShowSnackbar(e.message ?: "Error desconocido"))
                } finally {
                    isPaginating = false
                }
            }
        }

//        fun onMatchClick(hit: Hit) {
//            viewModelScope.launch {
//                _eventFlow.emit(HitsUiEvent.Navigate("matchDetail/${hit.matchId}"))
//            }
//        }
//
//        fun onBackPressed() {
//            viewModelScope.launch {
//                _eventFlow.emit(HitsUiEvent.PopBackStack)
//            }
//        }
    }
