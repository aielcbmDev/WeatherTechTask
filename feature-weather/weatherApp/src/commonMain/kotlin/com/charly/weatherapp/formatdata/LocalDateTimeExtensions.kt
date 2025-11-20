package com.charly.weatherapp.formatdata

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.number

private const val LENGTH = 2
private const val PAD_CHAR = '0'

/**
 * Formats a LocalDateTime into "dd/MM/yyyy" format.
 */
fun LocalDateTime.formatToString(): String {
    val day = day.toString().padStart(LENGTH, PAD_CHAR)
    val month = month.number.toString().padStart(LENGTH, PAD_CHAR)
    val year = year
    return "$day/$month/$year"
}
