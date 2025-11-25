package com.charly.domain.usecases

import com.charly.domain.OpenClassForMocking
import com.charly.domain.model.DailyForecast
import com.charly.domain.repositories.GetDailyWeatherForecastListRepository
import kotlinx.coroutines.flow.Flow

@OpenClassForMocking
class GetDailyWeatherForecastListUseCase(
    private val getDailyWeatherForecastListRepository: GetDailyWeatherForecastListRepository
) {

    suspend fun execute(invalidateCache: Boolean = false): Flow<List<DailyForecast>> {
        return getDailyWeatherForecastListRepository.execute(invalidateCache)
    }
}
