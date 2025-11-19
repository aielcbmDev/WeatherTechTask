package com.charly.weatherapp.ui

import androidx.lifecycle.ViewModel
import com.charly.domain.usecases.DailyWeatherForecastUseCase

class MainViewModel(
    private val weatherApiKey: String,
    private val dailyWeatherForecastUseCase: DailyWeatherForecastUseCase
) : ViewModel() {

    init {
        println("weatherApiKey: $weatherApiKey")
    }
}
