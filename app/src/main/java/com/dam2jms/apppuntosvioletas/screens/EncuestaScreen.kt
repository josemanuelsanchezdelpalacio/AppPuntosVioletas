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
fun EncuestaScreenBodyContent(
    modifier: Modifier,
    mvvm: ViewModelEncuestaScreen,
    navController: NavHostController,
    uiState: UiState
) {
    val preguntasEncuesta = PreguntasEncuesta()
    val preguntasRelacion = preguntasEncuesta.preguntas
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        if (uiState.showPentagon) {
            Box(
                modifier = Modifier
                    .size(300.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2, size.height / 2)
                    val radius = min(size.width, size.height) / 2

                    // Dibujar el pentágono de estadísticas
                    drawFullPentagon(this, center, radius, preguntasRelacion[uiState.currentQuestionIndex].opciones)

                    // Dibujar el borde del pentágono
                    drawStatisticPentagon(this, center, radius, uiState.statistics)
                }
            }
        } else {
            if (uiState.numPregunta < preguntasRelacion.size) {
                // Muestra la pregunta actual
                Text(
                    text = preguntasRelacion[uiState.numPregunta].enunciado,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(8.dp)
                )

                // Muestra los botones de radio junto a las opciones
                preguntasRelacion[uiState.numPregunta].opciones.forEachIndexed { index, opcion ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        RadioButton(
                            selected = index == uiState.respuestaSeleccionada,
                            onClick = { mvvm.seleccionarRespuesta(index) },
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .offset(y = 4.dp) // Ajuste para que aparezcan rectos
                        )
                        Text(text = opcion)
                    }
                }

                Button(
                    onClick = {
                        // Siguiente pregunta
                        mvvm.registrarRespuesta(uiState.respuestaSeleccionada)
                        mvvm.siguientePregunta()
                        Toast.makeText(context, "Respuesta registrada", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally) // Centrar el botón
                ) {
                    Text(text = "Siguiente Pregunta")
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            mvvm.reiniciarEncuesta()
                        }
                    ) {
                        Text(text = "Reiniciar Encuesta")
                    }

                    Button(
                        onClick = {
                            if (uiState.numPregunta == preguntasRelacion.size) {
                                mvvm.calcularPentagono()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Debe terminar la encuesta para comprobar",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    ) {
                        Text(text = "Comprobar")
                    }
                }
            }
        }
    }
}

fun drawStatisticPentagon(
    drawScope: androidx.compose.ui.graphics.drawscope.DrawScope,
    center: Offset,
    radius: Float,
    statistics: List<Int>
) {
    val angleStep = 72f
    var startAngle = -90f

    val statisticPentagon = Path().apply {
        for (i in 0 until 5) {
            val statValue = if (statistics[i] > 0) 1f else 0.5f
            val x = center.x + radius * statistics[i] / statistics.size * cos(Math.toRadians(startAngle.toDouble()).toFloat()) * statValue
            val y = center.y + radius * statistics[i] / statistics.size * sin(Math.toRadians(startAngle.toDouble()).toFloat()) * statValue
            if (startAngle == -90f) moveTo(x, y) else lineTo(x, y)
            startAngle += angleStep
        }
        close()
    }

    drawScope.drawPath(
        statisticPentagon,
        color = Color(android.graphics.Color.parseColor("#FF00FF")),
        style = androidx.compose.ui.graphics.drawscope.Fill
    )
}

private fun DrawScope.drawText(
    text: AnnotatedString,
    x: Float,
    y: Float,
    paint: androidx.compose.ui.graphics.Paint
) {
    drawContext.canvas.nativeCanvas.drawText(text.toString(), x, y, paint.asFrameworkPaint())
}

fun drawFullPentagon(
    drawScope: androidx.compose.ui.graphics.drawscope.DrawScope,
    center: Offset,
    radius: Float,
    answers: List<String>
) {
    val angleStep = 72f
    var startAngle = -90f

    val fullPentagon = Path().apply {
        for (i in answers.indices) {
            val x = center.x + radius * cos(Math.toRadians(startAngle.toDouble()).toFloat())
            val y = center.y + radius * sin(Math.toRadians(startAngle.toDouble()).toFloat())
            if (startAngle == -90f) moveTo(x, y) else lineTo(x, y)
            startAngle += angleStep
        }
        close()
    }

    drawScope.drawPath(
        fullPentagon,
        color = Color(android.graphics.Color.parseColor("#FF00FF")),
        style = androidx.compose.ui.graphics.drawscope.Stroke(4.dp.value)
    )

    val paint = Paint().apply {
        color = Color.Black.toArgb()
        textSize = 24f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    val textPaint = paint.asComposePaint()
    val textOffset = 20.dp
    val textRadius = radius + textOffset.value

    repeat(answers.size) {
        val angle = Math.toRadians(-90 + it * 72.toDouble()).toFloat()
        val x = center.x + textRadius * cos(angle)
        val y = center.y + textRadius * sin(angle)
        drawScope.drawText(
            text = AnnotatedString(answers[it]),
            x = x,
            y = y,
            paint = textPaint
        )
    }
}

