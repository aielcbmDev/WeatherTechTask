package com.something.volkswagentechtask

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.charly.weatherapp.di.weatherAppModule
import com.charly.weatherapp.ui.MainViewModel
import com.something.volkswagentechtask.di.appModule
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.dsl.KoinAppDeclaration

@Composable
@Preview
fun App(koinAppDeclaration: KoinAppDeclaration? = null) {
    KoinApplication(application = {
        koinAppDeclaration?.invoke(this)
        modules(appModule, weatherAppModule)
    }) {
        MaterialTheme {
            MainNavigationHost()
        }
    }
}

@Composable
private fun MainNavigationHost() {
    koinViewModel<MainViewModel>()
}
