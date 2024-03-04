package com.dam2jms.apppuntosvioletas.models

import android.graphics.Color.parseColor
import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asComposePaint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.dam2jms.apppuntosvioletas.states.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.cos
import kotlin.math.sin

class ViewModelEncuestaScreen : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun reiniciarEncuesta() { _uiState.value = UiState() }

    fun seleccionarRespuesta(respuesta: Int) { _uiState.value = _uiState.value.copy(respuestaSeleccionada = respuesta) }

    fun registrarRespuesta(respuesta: Int) {
        _uiState.value = _uiState.value.copy(
            respuestasSeleccionadas = _uiState.value.respuestasSeleccionadas + respuesta,
            respuestaSeleccionada = -1
        )
    }

    fun siguientePregunta() {
        _uiState.value = _uiState.value.copy(numPregunta = _uiState.value.numPregunta + 1)
    }

    //metodo para calcular la puntuacion segun las respuestas y dibuja las aristas del pentagono interior
    fun calcularPentagono() {
        val respuestasPuntuacion = _uiState.value.respuestasSeleccionadas.groupingBy { it }.eachCount()
        val estadisticas = List(5) { respuestasPuntuacion.getOrDefault(it, 0) }
        _uiState.value = _uiState.value.copy(estatisticas = estadisticas, mostrarPentagono = true)
    }

    //dibuja el pentagono interno que es el que se dibuja calculando las estadisticas
    fun drawStatisticPentagon(
        drawScope: DrawScope,
        centro: Offset,
        radio: Float,
        estadisticas: List<Int>
    ) {
        val lineaEntreAngulo = 72f
        var inicioAngulo = -90f

        val pentagonoEstadisticas = Path().apply {
            for (i in 0 until 5) {
                val inicioAngulo = if (estadisticas[i] > 0) 1f else 0.5f
                val x = centro.x + radio * statistics[i] / estadisticas.size * cos(Math.toRadians(inicioAngulo.toDouble()).toFloat()) * statValue
                val y = centro.y + radio * statistics[i] / estadisticas.size * sin(Math.toRadians(inicioAngulo.toDouble()).toFloat()) * statValue
                if (inicioAngulo == -90f) moveTo(x, y) else lineTo(x, y)
                inicioAngulo += lineaEntreAngulo
            }
            close()
        }

        drawScope.drawPath(
            pentagonoEstadisticas,
            color = Color(parseColor("#FF00FF")),
            style = Fill
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

    //dibuja el pentagono exterior
    fun drawFullPentagon(drawScope: DrawScope, center: Offset, radius: Float, respuestas: List<String>) {
        val lineaEntreAngulo = 72f
        var inicioAngulo = -90f

        val pentagonoGrande = Path().apply {
            for (i in respuestas.indices) {
                val x = center.x + radius * cos(Math.toRadians(inicioAngulo.toDouble()).toFloat())
                val y = center.y + radius * sin(Math.toRadians(inicioAngulo.toDouble()).toFloat())
                if (inicioAngulo == -90f) moveTo(x, y) else lineTo(x, y)
                inicioAngulo += lineaEntreAngulo
            }
            close()
        }

        //modifico el color y el tamaño del pentagono
        drawScope.drawPath(pentagonoGrande, color = Color(parseColor("#FF00FF")), style = Stroke(4.dp.value))

        //modifico el tamaño, el tipo y el color de las estadisticas de los vertices
        val paint = Paint().apply {
            color = androidx.compose.ui.graphics.Color.Black.toArgb()
            textSize = 24f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val textPaint = paint.asComposePaint()
        val textOffset = 20.dp
        val texto = radius + textOffset.value

        for (i in respuestas.indices) {
            val angulo = Math.toRadians(-90 + i * 72.toDouble()).toFloat()
            val x = center.x + texto * cos(angulo)
            val y = center.y + texto * sin(angulo)
            drawScope.drawText(text = AnnotatedString(respuestas[i]), x = x, y = y, paint = texto)
        }
    }
}
