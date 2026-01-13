package com.charly.weatherapp.formatdata.speed

import com.charly.weatherapp.OpenClassForMocking
import com.charly.weatherapp.model.WeatherUnits
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import weathertechtask.feature_weather.weatherapp.generated.resources.Res
import weathertechtask.feature_weather.weatherapp.generated.resources.display_data_with_units_text
import weathertechtask.feature_weather.weatherapp.generated.resources.imperial_speed_units
import weathertechtask.feature_weather.weatherapp.generated.resources.metric_speed_units
import weathertechtask.feature_weather.weatherapp.generated.resources.standard_speed_units

@OpenClassForMocking
class SpeedFormatter(
    private val weatherUnits: WeatherUnits
) {

    suspend fun formatSpeed(speed: String?): String? {
        if (speed.isNullOrEmpty()) return null
        return when (weatherUnits) {
            WeatherUnits.STANDARD -> getSpeedFormated(speed, Res.string.standard_speed_units)
            WeatherUnits.METRIC -> getSpeedFormated(speed, Res.string.metric_speed_units)
            WeatherUnits.IMPERIAL -> getSpeedFormated(speed, Res.string.imperial_speed_units)
        }

    }

    private suspend fun getSpeedFormated(
        speed: String,
        speedUnits: StringResource
    ): String {
        val units = getString(speedUnits)
        return getString(Res.string.display_data_with_units_text, speed, units)
    }
}
