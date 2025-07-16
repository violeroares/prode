package com.rockandcode.prodefutbolero.utils

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun formatFechaHora(fechaIso: String): String =
    try {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val localDateTime = LocalDateTime.parse(fechaIso, formatter)
        localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
    } catch (e: Exception) {
        "Fecha inválida ${e.message}"
    }

// fun Match.dateAsLocalDateTime(): LocalDateTime = LocalDateTime.parse(this.date, DateTimeFormatter.ISO_DATE_TIME)

fun ordinalEs(number: Int): String =
    when (number) {
        1 -> "${number}ra"
        2 -> "${number}da"
        3 -> "${number}ra"
        else -> "${number}ta"
    }

fun obtenerSaludo(): String {
    // val horaActual = LocalTime.now().hour
    val horaActual = LocalTime.now(ZoneId.of("America/Argentina/Buenos_Aires")).hour
    return when (horaActual) {
        in 6..11 -> "¡Buenos Días!"
        in 12..18 -> "¡Buenas Tardes!"
        else -> "¡Buenas Noches!"
    }
}
