package com.charly.weatherapp.formatdata.datetime

fun interface DateFormatter {

    fun formatEpochSecondsToDateString(epochSeconds: Long?): String?
}
