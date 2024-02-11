package com.dam2jms.apppuntosvioletas.navigation
sealed class AppScreens (val route: String){

    object FirstScreen: AppScreens(route = "first_screen")
    object EncuestaScreen: AppScreens(route = "encuesta_screen")
    object SOS_Screen: AppScreens(route = "sos_screen")
    object OpcionesScreen: AppScreens(route = "opciones_screen")
}
