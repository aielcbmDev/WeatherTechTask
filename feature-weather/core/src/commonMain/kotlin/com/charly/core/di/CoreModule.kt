package com.charly.core.di

import com.charly.core.cache.TimerCache
import com.charly.core.repositories.DailyWeatherForecastRepositoryImpl
import com.charly.database.di.databaseModule
import com.charly.datastore.di.datastoreModule
import com.charly.domain.repositories.DailyWeatherForecastRepository
import com.charly.networking.di.networkingModule
import org.koin.dsl.module
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
val coreModule = module {
    includes(databaseModule)
    includes(datastoreModule)
    includes(networkingModule)

    single<Clock> { Clock.System }
    single<TimerCache> {
        TimerCache(
            clock = get(),
            datastoreDataSource = get()
        )
    }
    single<DailyWeatherForecastRepository> {
        DailyWeatherForecastRepositoryImpl(
            timerCache = get(),
            weatherDatabaseDataSource = get(),
            weatherNetworkDataSource = get()
        )
    }
}
