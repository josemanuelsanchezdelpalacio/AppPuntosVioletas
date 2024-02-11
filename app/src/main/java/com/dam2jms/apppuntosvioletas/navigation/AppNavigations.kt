package com.dam2jms.apppuntosvioletas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dam2jms.apppuntosvioletas.models.ViewModelEncuestaScreen
import com.dam2jms.apppuntosvioletas.models.ViewModelFirstScreen
import com.dam2jms.apppuntosvioletas.models.ViewModelSOS_Screen
import com.dam2jms.apppuntosvioletas.screens.EncuestaScreen
import com.dam2jms.apppuntosvioletas.screens.FirstScreen
import com.dam2jms.apppuntosvioletas.screens.OpcionesScreen
import com.dam2jms.apppuntosvioletas.screens.SOS_Screen

@Composable
fun appNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.FirstScreen.route) {
        composable(route = AppScreens.FirstScreen.route) { FirstScreen(navController, mvvm = ViewModelFirstScreen()) }
        composable(route = AppScreens.EncuestaScreen.route) { EncuestaScreen(navController, mvvm = ViewModelEncuestaScreen()) }
        composable(route = AppScreens.SOS_Screen.route) { SOS_Screen(navController, mvvm = ViewModelSOS_Screen()) }
        composable(route = AppScreens.OpcionesScreen.route) { OpcionesScreen(navController) }
    }
}
