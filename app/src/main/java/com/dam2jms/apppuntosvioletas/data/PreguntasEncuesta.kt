package com.dam2jms.apppuntosvioletas.data

data class Pregunta(val enunciado: String, val opciones: List<String>)

class PreguntasEncuesta {
    val preguntas: List<Pregunta> = listOf(
        Pregunta(
            "¿Qué te parece la aplicación?",
            listOf("Muy buena", "Buena", "Regular", "Mala", "Muy mala")
        ),
        Pregunta(
            "¿Qué te parece el diseño?",
            listOf("Muy bueno", "Bueno", "Regular", "Malo", "Muy malo")
        ),
        Pregunta(
            "¿Qué te parece la usabilidad?",
            listOf("Muy buena", "Buena", "Regular", "Mala", "Muy mala")
        ),
        Pregunta(
            "¿Qué te parece el contenido?",
            listOf("Muy bueno", "Bueno", "Regular", "Malo", "Muy malo")
        ),
        Pregunta(
            "¿Qué te parece la funcionalidad?",
            listOf("Muy buena", "Buena", "Regular", "Mala", "Muy mala")
        ),
    )
}
