package com.rockandcode.prodefutbolero.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rockandcode.prodefutbolero.domain.tournament.models.Match
import com.rockandcode.prodefutbolero.domain.tournament.models.MatchDate
import com.rockandcode.prodefutbolero.domain.tournament.repository.ITournamentRepository
import com.rockandcode.prodefutbolero.domain.tournament.usecases.GetMatchesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchesViewModel
    @Inject
    constructor(
        private val getMatchesUseCase: GetMatchesUseCase,
        private val tournamentRepository: ITournamentRepository,
    ) : ViewModel() {
        var matches by mutableStateOf<List<Match>>(emptyList())
            private set

        var selectedDateId by mutableStateOf<Int?>(null)
        var dates by mutableStateOf<List<MatchDate>>(emptyList())

        var currentPage by mutableIntStateOf(1)
        var totalPages by mutableIntStateOf(1)

        var isLoading by mutableStateOf(false)
        var isRefreshing by mutableStateOf(false)
        var isPaginating by mutableStateOf(false)

        private val isBusy get() = isLoading || isRefreshing || isPaginating

        var searchQuery by mutableStateOf("")
            private set

        fun loadDates(tournamentId: Int) {
            viewModelScope.launch {
                dates = tournamentRepository.getDates(tournamentId)
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
            dateId: Int?,
            isPullToRefresh: Boolean = false,
        ) {
            if (isBusy) return

            currentPage = 1
            totalPages = 1
            matches = emptyList()

            viewModelScope.launch {
                if (isPullToRefresh) {
                    isRefreshing = true
                } else {
                    isLoading = true
                }

                try {
                    val result =
                        getMatchesUseCase(
                            tournamentId,
                            currentPage,
                            teamName = teamName,
                            dateId = dateId,
                        )
                    matches = result.matches
                    totalPages = result.totalPages
                    currentPage++
                } finally {
                    isRefreshing = false
                    isLoading = false
                }
            }
        }

        fun loadNextPage(
            tournamentId: Int?,
            teamName: String? = null,
            dateId: Int?,
        ) {
            if (isBusy || currentPage > totalPages) return

            viewModelScope.launch {
                isPaginating = true
                try {
                    val result =
                        getMatchesUseCase(
                            tournamentId,
                            currentPage,
                            teamName = teamName,
                            dateId = dateId,
                        )
                    matches = (matches + result.matches).distinctBy { it.matchId }
                    totalPages = result.totalPages
                    currentPage++
                } finally {
                    isPaginating = false
                }
            }
        }
    }
