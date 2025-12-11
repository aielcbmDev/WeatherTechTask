package com.charly.datastore.di

import com.charly.datastore.datasource.DatastoreDataSource
import org.koin.core.module.Module
import org.koin.dsl.module

internal expect val datastorePlatformModule: Module

val datastoreModule = module {
    includes(datastorePlatformModule)
    single<DatastoreDataSource> { DatastoreDataSource(dataStore = get()) }
}
