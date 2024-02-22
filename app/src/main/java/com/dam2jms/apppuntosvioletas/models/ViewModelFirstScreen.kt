package com.dam2jms.apppuntosvioletas.models

import androidx.lifecycle.ViewModel
import com.dam2jms.apppuntosvioletas.states.UiState
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ViewModelFirstScreen : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun updateMapState(coordenadas: LatLng, puntoVioleta1: LatLng) {
        _uiState.value = _uiState.value.copy(coordenadas = coordenadas, puntoVioleta1 = puntoVioleta1)
    }
}