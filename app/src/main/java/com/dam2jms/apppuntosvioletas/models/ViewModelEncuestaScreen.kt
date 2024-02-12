package com.dam2jms.apppuntosvioletas.models

import androidx.lifecycle.ViewModel
import com.dam2jms.apppuntosvioletas.states.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ViewModelEncuestaScreen : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun reiniciarEncuesta() {
        _uiState.value = UiState()
    }

    fun seleccionarRespuesta(respuesta: Int) {
        _uiState.value = _uiState.value.copy(respuestaSeleccionada = respuesta)
    }

    fun registrarRespuesta(respuesta: Int) {
        _uiState.value = _uiState.value.copy(
            respuestasSeleccionadas = _uiState.value.respuestasSeleccionadas + respuesta,
            respuestaSeleccionada = -1
        )
    }

    fun siguientePregunta() {
        _uiState.value = _uiState.value.copy(numPregunta = _uiState.value.numPregunta + 1)
    }

    fun mostrarPentagono() {
        _uiState.value = _uiState.value.copy(showPentagon = true)
    }

    fun calcularPentagono() {
        val answerCounts = _uiState.value.respuestasSeleccionadas.groupingBy { it }.eachCount()
        val statistics = List(5) { answerCounts.getOrDefault(it, 0) }
        _uiState.value = _uiState.value.copy(statistics = statistics, showPentagon = true)
    }
}
