package com.dam2jms.apppuntosvioletas.states

import com.google.android.gms.maps.model.LatLng


data class UiState(
    val puntoVioleta1: LatLng = LatLng(42.52306378665287, -0.36470226296963065),
    val puntoVioleta2: LatLng = LatLng(42.51816522796081, -0.3644260015864647),

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