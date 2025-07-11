package com.rockandcode.prodefutbolero.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rockandcode.prodefutbolero.domain.tournament.models.Tournament
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

        fun selectTournament(tournament: Tournament) {
            _selectedTournament.value = tournament
        }

        fun logout() {
            viewModelScope.launch {
                clearSessionUseCase()
            }
        }
    }
