package com.charly.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.charly.datastore.createDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val datastorePlatformModule: Module
    get() = module {
        single<DataStore<Preferences>> { createDataStore(context = get()) }
    }
