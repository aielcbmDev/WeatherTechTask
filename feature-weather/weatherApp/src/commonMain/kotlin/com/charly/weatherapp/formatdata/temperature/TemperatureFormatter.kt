package com.charly.weatherapp.formatdata.temperature

import com.charly.weatherapp.OpenClassForMocking
import com.charly.weatherapp.model.WeatherUnits
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import weathertechtask.feature_weather.weatherapp.generated.resources.Res
import weathertechtask.feature_weather.weatherapp.generated.resources.display_data_with_units_text
import weathertechtask.feature_weather.weatherapp.generated.resources.imperial_temperature_units
import weathertechtask.feature_weather.weatherapp.generated.resources.metric_temperature_units
import weathertechtask.feature_weather.weatherapp.generated.resources.standard_temperature_units

@OpenClassForMocking
class TemperatureFormatter(
    private val weatherUnits: WeatherUnits
) {
    suspend fun formatTemperature(temperature: String?): String? {
        if (temperature.isNullOrEmpty()) return null
        return when (weatherUnits) {
            WeatherUnits.STANDARD -> getTemperatureFormated(
                temperature = temperature,
                temperatureUnits = Res.string.standard_temperature_units
            )

            WeatherUnits.METRIC -> getTemperatureFormated(
                temperature = temperature,
                temperatureUnits = Res.string.metric_temperature_units
            )

            WeatherUnits.IMPERIAL -> getTemperatureFormated(
                temperature = temperature,
                temperatureUnits = Res.string.imperial_temperature_units
            )
        }
    }

    private suspend fun getTemperatureFormated(
        temperature: String,
        temperatureUnits: StringResource
    ): String {
        val units = getString(temperatureUnits)
        return getString(Res.string.display_data_with_units_text, temperature, units)
    }
}
