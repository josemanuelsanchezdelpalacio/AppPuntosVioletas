package com.dam2jms.apppuntosvioletas.states

import com.google.android.gms.maps.model.LatLng


data class UiState(
    val coordenadas: LatLng = LatLng(38.35454820180169, -0.6675578816145887),
    val puntoVioleta1: LatLng = LatLng(42.51821260084617, -0.3644367453749489),

    val isBotonEncuesta: Boolean = false,
    val isBotonSOS: Boolean = false,
    val isMenuDesplegable: Boolean = false,

    var numPregunta: Int = 0,
    var respuestaSeleccionada: Int = -1,
    var respuestasSeleccionadas: List<Int> = listOf(),
    var estatisticas: List<Int> = listOf(),
    var mostrarPentagono: Boolean = false,
    var preguntaActual: Int = 0
)