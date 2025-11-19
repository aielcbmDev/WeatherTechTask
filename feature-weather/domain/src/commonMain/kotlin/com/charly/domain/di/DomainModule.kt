package com.charly.domain.di

import com.charly.domain.usecases.DailyWeatherForecastUseCase
import org.koin.dsl.module

val domainModule = module {
    factory<DailyWeatherForecastUseCase> {
        DailyWeatherForecastUseCase(
            dailyWeatherForecastRepository = get()
        )
    }
}
