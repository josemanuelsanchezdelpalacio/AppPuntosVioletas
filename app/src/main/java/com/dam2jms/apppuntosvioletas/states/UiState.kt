package com.dam2jms.apppuntosvioletas.states

import com.google.android.gms.maps.model.LatLng


data class UiState(
    val coordenadas: LatLng = LatLng(42.57286, 0.56419),
    val puntoVioleta1: LatLng = LatLng(42.3115, -0.229),
    val isBotonEncuesta: Boolean = false,
    val isBotonSOS: Boolean = false,
    val isMenuDesplegable: Boolean = false,
    var respuesta: Int = 0,
    var puntuacion: Int = 0,
    var numPregunta: Int = 0,
    var respuestaSeleccionada: Int = -1,
    var respuestasSeleccionadas: List<Int> = listOf(),
    var statistics: List<Int> = listOf(),
    var showPentagon: Boolean = false,
    var currentQuestionIndex: Int = 0
)