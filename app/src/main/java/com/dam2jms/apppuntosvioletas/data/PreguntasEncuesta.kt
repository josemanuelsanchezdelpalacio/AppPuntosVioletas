package com.dam2jms.apppuntosvioletas.data

data class Pregunta(val enunciado: String, val opciones: List<String>)

class PreguntasEncuesta {
    val preguntas: List<Pregunta> = listOf(
        Pregunta(
            "¿Cómo evalúas la comunicación en tu relación?",
            listOf("Muy buena", "Buena", "Regular", "Mala", "Muy mala")
        ),
        Pregunta(
            "¿Cómo describirías el nivel de confianza en tu relación?",
            listOf("Muy buena", "Buena", "Regular", "Mala", "Muy mala")
        ),
        Pregunta(
            "¿Qué tan satisfecho/a estás con el apoyo emocional en tu relación?",
            listOf("Muy buena", "Buena", "Regular", "Mala", "Muy mala")
        ),
        Pregunta(
            "¿Cómo evalúas la resolución de conflictos en tu relación?",
            listOf("Muy buena", "Buena", "Regular", "Mala", "Muy mala")
        ),
        Pregunta(
            "¿Qué tan presente se siente tu pareja en tu vida diaria?",
            listOf("Muy buena", "Buena", "Regular", "Mala", "Muy mala")
        ),
    )
}

