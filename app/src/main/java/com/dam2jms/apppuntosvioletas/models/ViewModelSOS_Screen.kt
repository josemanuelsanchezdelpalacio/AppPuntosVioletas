package com.dam2jms.apppuntosvioletas.models

import androidx.lifecycle.ViewModel
import com.dam2jms.apppuntosvioletas.states.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ViewModelSOS_Screen : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun llamada112() {
        TODO("Not yet implemented")
    }

    fun alarma() {
        TODO("Not yet implemented")
    }

    fun mandarUbicacionContacto() {
        TODO("Not yet implemented")
    }

    fun abrirMenu() {
        val newState = _uiState.value.copy(isMenuDesplegable = true)
        _uiState.value = newState
    }

    fun cerrarMenu() {
        val newState = _uiState.value.copy(isMenuDesplegable = false)
        _uiState.value = newState
    }
}