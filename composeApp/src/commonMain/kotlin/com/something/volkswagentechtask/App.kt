package com.something.volkswagentechtask

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.charly.weatherapp.di.weatherAppModule
import com.charly.weatherapp.ui.mainscreen.MainScreen
import com.charly.weatherapp.ui.mainscreen.MainViewModel
import com.charly.weatherapp.ui.mainscreen.ViewIntent
import com.something.volkswagentechtask.di.appModule
import com.something.volkswagentechtask.navigation.Destination
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
    val navController = rememberNavController()
    NavHost(
        navController,
        startDestination = Destination.Home.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = Destination.Home.route) {
            val mainViewModel = koinViewModel<MainViewModel>()
            val mainScreenState by mainViewModel.state.collectAsStateWithLifecycle()
            MainScreen(
                mainScreenState = mainScreenState,
                onDailyForecastModelClick = { id ->
                    navController.navigate(Destination.Detail(id).route)
                },
                onRetryButtonClicked = { mainViewModel.handleIntent(ViewIntent.FetchDailyWeatherForecast) }
            )
        }
        composable(
            route = Destination.Detail.ROUTE_PATTERN,
            arguments = listOf(navArgument(Destination.Detail.ARG_ITEM_ID) {
                type = NavType.LongType
            }),
        ) { backStackEntry ->
            val savedStateHandle = backStackEntry.savedStateHandle
            savedStateHandle.get<Long>(Destination.Detail.ARG_ITEM_ID)!!
        }
    }
}
