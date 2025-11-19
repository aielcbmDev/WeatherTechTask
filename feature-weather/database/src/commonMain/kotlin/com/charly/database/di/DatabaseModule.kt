package com.charly.database.di

import androidx.room.RoomDatabase
import com.charly.database.WeatherDatabase
import com.charly.database.datasources.WeatherDatabaseDataSource
import com.charly.database.utils.getRoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

internal expect val databasePlatformModule: Module

val databaseModule = module {
    includes(databasePlatformModule)

    single<WeatherDatabase> {
        get<RoomDatabase.Builder<WeatherDatabase>>().getRoomDatabase()
    }

    factory<WeatherDatabaseDataSource> {
        val dailyDao = get<WeatherDatabase>().getDailyDao()
        WeatherDatabaseDataSource(dailyDao)
    }
}
