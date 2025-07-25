package com.rockandcode.prodefutbolero.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rockandcode.prodefutbolero.domain.prediction.models.Prediction
import com.rockandcode.prodefutbolero.domain.prediction.repository.IPredictionRepository
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

sealed interface PredictionsUiState {
    data object Loading : PredictionsUiState

    data class Success(
        val predictions: List<Prediction>,
    ) : PredictionsUiState

    data class Error(
        val message: String,
    ) : PredictionsUiState
}

data class PredictionsScreenState(
    val uiState: PredictionsUiState = PredictionsUiState.Loading,
    val isRefreshing: Boolean = false,
)

sealed class PredictionsUiEvent {
    data class ShowSnackbar(
        val message: String,
    ) : PredictionsUiEvent()

    data class Navigate(
        val route: String,
    ) : PredictionsUiEvent()

    data object PopBackStack : PredictionsUiEvent()
}

@HiltViewModel
class MyPredictionsViewModel
    @Inject
    constructor(
        private val predictionRepository: IPredictionRepository,
    ) : ViewModel() {
        private val _screenState = MutableStateFlow(PredictionsScreenState())
        val screenState: StateFlow<PredictionsScreenState> = _screenState.asStateFlow()

        private val _eventFlow = MutableSharedFlow<PredictionsUiEvent>()
        val eventFlow: SharedFlow<PredictionsUiEvent> = _eventFlow.asSharedFlow()

        private var userId: String? = null
        private var tournamentId: Int? = null

        fun setContext(
            userId: String?,
            tournamentId: Int?,
        ) {
            this.userId = userId
            this.tournamentId = tournamentId
        }

//        var searchQuery by mutableStateOf("")
//            private set

        var selectedDateId by mutableStateOf<Int?>(null)
            private set

        fun setSelectedDate(dateId: Int?) {
            selectedDateId = dateId
            // getPredictions(teamName = searchQuery, dateId = selectedDateId)
            getPredictions(dateId = selectedDateId)
        }

        private val _selectedPrediction = MutableStateFlow<Prediction?>(null)
        val selectedPrediction: StateFlow<Prediction?> = _selectedPrediction.asStateFlow()

        private val _localGoals = MutableStateFlow<Int?>(null)
        val localGoals: StateFlow<Int?> = _localGoals

        private val _visitorGoals = MutableStateFlow<Int?>(null)
        val visitorGoals: StateFlow<Int?> = _visitorGoals

        fun selectPrediction(prediction: Prediction) {
            _selectedPrediction.value = prediction
            _localGoals.value = prediction.localGoals
            _visitorGoals.value = prediction.visitorGoals
        }

        fun setLocalGoals(goals: Int?) {
            _localGoals.value = goals
        }

        fun setVisitorGoals(goals: Int?) {
            _visitorGoals.value = goals
        }

        fun clearSelectedPrediction() {
            _selectedPrediction.value = null
        }

        fun getPredictions(
            // teamName: String? = null,
            dateId: Int? = selectedDateId,
        ) {
            viewModelScope.launch {
                try {
                    val result =
                        predictionRepository.getPredictionsApp(
                            userId = userId.toString(),
                            tournamentId = tournamentId.toString(),
                            dateId = dateId.toString(),
                        )
                    _screenState.update {
                        it.copy(
                            uiState = PredictionsUiState.Success(predictions = result),
                            isRefreshing = false,
                        )
                    }
                } catch (e: Exception) {
                    _screenState.update {
                        it.copy(
                            uiState = PredictionsUiState.Error(e.message ?: "Error desconocido"),
                            isRefreshing = false,
                        )
                    }
                    _eventFlow.emit(PredictionsUiEvent.ShowSnackbar(e.message ?: "Error desconocido"))
                }
            }
        }

//        fun onPredictionClick(prediction: Prediction) {
//            selectPrediction(prediction) // 👉 ahora selecciona antes de navegar
//            viewModelScope.launch {
//                _eventFlow.emit(PredictionsUiEvent.Navigate("edit_prediction"))
//            }
//        }

        fun updatePredictionGoals(
            predictionId: Int?,
            localGoals: Int,
            visitorGoals: Int,
        ) {
            if (predictionId == null) return

            val currentState = _screenState.value
            if (currentState.uiState is PredictionsUiState.Success) {
                val updatedList =
                    currentState.uiState.predictions.map {
                        if (it.predictionId == predictionId) {
                            it.copy(localGoals = localGoals, visitorGoals = visitorGoals)
                        } else {
                            it
                        }
                    }

                _screenState.value =
                    currentState.copy(
                        uiState = PredictionsUiState.Success(updatedList),
                    )

                // También actualizamos la predicción seleccionada si coincide
                if (_selectedPrediction.value?.predictionId == predictionId) {
                    _selectedPrediction.value =
                        _selectedPrediction.value?.copy(
                            localGoals = localGoals,
                            visitorGoals = visitorGoals,
                        )
                }
            }
        }

        fun onBackPressed() {
            clearSelectedPrediction() // 👉 limpiamos cuando se vuelve
            viewModelScope.launch {
                _eventFlow.emit(PredictionsUiEvent.PopBackStack)
            }
        }
    }
