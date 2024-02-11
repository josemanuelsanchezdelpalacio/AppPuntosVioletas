package com.dam2jms.apppuntosvioletas.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.dam2jms.apppuntosvioletas.models.ViewModelSOS_Screen
import com.dam2jms.apppuntosvioletas.navigation.AppScreens
import com.dam2jms.apppuntosvioletas.states.UiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SOS_Screen(navController: NavHostController, mvvm: ViewModelSOS_Screen) {

    val uiState by mvvm.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController = navController)
        },
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("App Puntos Violetas") },
                    navigationIcon = {
                        //icono del menu para controlar la apertura y cierre del menu lateral
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(route = AppScreens.SOS_Screen.route) },
                    containerColor = Color(0xFFAC53F7),
                    elevation = FloatingActionButtonDefaults.elevation()
                ) {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = "SOS")
                }
            }
        ) { paddingValues ->
            SOS_ScreenBodyContent(modifier = Modifier.padding(paddingValues), mvvm, navController, uiState)
        }
    }
}

@Composable
fun SOS_ScreenBodyContent(modifier: Modifier, mvvm: ViewModelSOS_Screen, navController: NavHostController, uiState: UiState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { mvvm.llamada112() }) {
            Text("Llamada 112")
        }
        Button(onClick = { mvvm.alarma() }) {
            Text("Alarma")
        }
        Button(onClick = { mvvm.mandarUbicacionContacto() }) {
            Text("Mandar ubicacion a contacto")
        }
    }
}

/*
* @Composable
fun WordleGame() {
    val word = "apple"  // La palabra a adivinar
    val guesses = remember { mutableStateOf(List(5) { "" }) }
    val colors = remember { mutableStateOf(List(5) { Color.Gray }) }

    Column {
        guesses.value.forEachIndexed { index, guess ->
            WordleRow(guess, colors.value[index]) { newGuess ->
                guesses.value = guesses.value.toMutableList().also { it[index] = newGuess }.toList()
                colors.value = colors.value.toMutableList().also { it[index] = getGuessColor(word, newGuess) }.toList()
            }
        }
    }
}

@Composable
fun WordleRow(guess: String, color: Color, onGuessChange: (String) -> Unit) {
    Row {
        repeat(5) { index ->
            OutlinedTextField(
                value = if (index < guess.length) guess[index].toString() else "",
                onValueChange = { if (it.length == 1) onGuessChange(if (index < guess.length) guess.replaceRange(index, index + 1, it) else guess + it) },
                colors = TextFieldDefaults.outlinedTextFieldColors(unfocusedBorderColor = color, focusedBorderColor = color),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                modifier = Modifier.size(50.dp)
            )
        }
    }
}

fun getGuessColor(word: String, guess: String): Color {
    if (guess.length != word.length) return Color.Gray
    if (guess == word) return Color.Green
    if (guess.any { it in word }) return Color.Yellow
    return Color.Red
}
* */
