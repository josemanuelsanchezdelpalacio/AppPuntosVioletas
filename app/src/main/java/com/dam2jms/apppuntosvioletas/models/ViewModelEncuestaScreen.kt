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

    fun calcularPentagono() {
        val answerCounts = _uiState.value.respuestasSeleccionadas.groupingBy { it }.eachCount()
        val statistics = List(5) { answerCounts.getOrDefault(it, 0) }
        _uiState.value = _uiState.value.copy(estatisticas = statistics, mostrarPentagono = true)
    }

    fun drawStatisticPentagon(
        drawScope: DrawScope,
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
            color = Color(parseColor("#FF00FF")),
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

    fun drawFullPentagon(drawScope: DrawScope, center: Offset, radius: Float, respuestas: List<String>) {
        val angleStep = 72f
        var startAngle = -90f

        val pentagonoGrande = Path().apply {
            for (i in respuestas.indices) {
                val x = center.x + radius * cos(Math.toRadians(startAngle.toDouble()).toFloat())
                val y = center.y + radius * sin(Math.toRadians(startAngle.toDouble()).toFloat())
                if (startAngle == -90f) moveTo(x, y) else lineTo(x, y)
                startAngle += angleStep
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
        val textRadius = radius + textOffset.value

        for (i in respuestas.indices) {
            val angulo = Math.toRadians(-90 + i * 72.toDouble()).toFloat()
            val x = center.x + textRadius * cos(angulo)
            val y = center.y + textRadius * sin(angulo)
            drawScope.drawText(text = AnnotatedString(respuestas[i]), x = x, y = y, paint = textPaint)
        }
    }
}
