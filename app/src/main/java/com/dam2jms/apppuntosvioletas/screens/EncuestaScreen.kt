package com.dam2jms.apppuntosvioletas.screens

import android.graphics.Paint
import android.graphics.Typeface
import com.dam2jms.apppuntosvioletas.data.PreguntasEncuesta
import com.dam2jms.apppuntosvioletas.models.ViewModelEncuestaScreen
import com.dam2jms.apppuntosvioletas.navigation.AppScreens
import com.dam2jms.apppuntosvioletas.states.UiState
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asComposePaint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncuestaScreen(navController: NavHostController, mvvm: ViewModelEncuestaScreen) {

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
            EncuestaScreenBodyContent(
                modifier = Modifier.padding(paddingValues),
                mvvm,
                navController,
                uiState
            )
        }
    }
}

@Composable
fun EncuestaScreenBodyContent(modifier: Modifier, mvvm: ViewModelEncuestaScreen, navController: NavHostController, uiState: UiState) {
    val preguntasEncuesta = PreguntasEncuesta()
    val preguntasLista = preguntasEncuesta.preguntas
    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        //cuando se contesten todas las preguntas muestra el pentagono
        if (uiState.mostrarPentagono) {
            Box(
                modifier = Modifier.size(300.dp)
            ) {
                //dibujo ambos pentagonos
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2, size.height / 2)
                    val radius = min(size.width, size.height) / 2
                    mvvm.drawFullPentagon(this, center, radius, preguntasLista[uiState.preguntaActual].opciones)
                    mvvm.drawStatisticPentagon(this, center, radius, uiState.estatisticas)
                }

                //boton para volver cuando ya se haya terminado la encuesta
                Button(
                    onClick = { navController.navigate(route = AppScreens.FirstScreen.route) },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Text(text = "Volver")
                }
            }
        } else {
            // Mientras que la pregunta actual es menor a la cantidad de preguntas en la lista, muestra las preguntas
if (uiState.numPregunta < preguntasLista.size) {
    Text(
        text = preguntasLista[uiState.numPregunta].enunciado,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(8.dp)
    )

    // Utilizando un bucle for para recorrer las opciones de la pregunta actual
    for (index in preguntasLista[uiState.numPregunta].opciones.indices) {
        val opcion = preguntasLista[uiState.numPregunta].opciones[index]

        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = index == uiState.respuestaSeleccionada,
                onClick = { mvvm.seleccionarRespuesta(index) },
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = opcion)
        }
    }

    Button(
        onClick = {
            if (uiState.respuestaSeleccionada == -1) {
                Toast.makeText(context, "Debes seleccionar una respuesta", Toast.LENGTH_SHORT).show()
            } else {
                mvvm.registrarRespuesta(uiState.respuestaSeleccionada)
                mvvm.siguientePregunta()

                if (uiState.numPregunta == preguntasLista.size - 1) {
                    // Si es la última pregunta, realiza la acción de comprobar
                    mvvm.calcularPentagono()
                }

                Toast.makeText(context, "Respuesta registrada", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
            ) {
                Text(text = if (uiState.numPregunta == preguntasLista.size - 1) "Comprobar" else "Siguiente Pregunta")
            }

            // Botón para reiniciar encuesta
            Button(
                onClick = { mvvm.reiniciarEncuesta() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Reiniciar Encuesta")
                }
            }
        }
    }
}

