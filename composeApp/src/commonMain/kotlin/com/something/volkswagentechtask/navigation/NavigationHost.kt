package com.something.volkswagentechtask.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.charly.weatherapp.ui.detail.DetailViewIntent
import com.charly.weatherapp.ui.detail.DetailViewModel
import com.charly.weatherapp.ui.detail.screen.DetailScreen
import com.charly.weatherapp.ui.main.MainViewIntent
import com.charly.weatherapp.ui.main.MainViewModel
import com.charly.weatherapp.ui.main.screen.MainScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WeatherNavigationHost() {
    val navController = rememberNavController()
    NavHost(
        navController,
        startDestination = Destination.Main.route
    ) {
        composable(route = Destination.Main.route) {
            val mainViewModel = koinViewModel<MainViewModel>()
            val mainScreenState by mainViewModel.state.collectAsStateWithLifecycle()
            MainScreen(
                mainScreenState = mainScreenState,
                onDailyForecastModelClick = { id ->
                    navController.navigate(Destination.Detail(id).route)
                },
                onRetryButtonClicked = {
                    mainViewModel.handleIntent(MainViewIntent.FetchDailyWeatherForecast(true))
                }
            )
        }
        composable(
            route = Destination.Detail.ROUTE_PATTERN,
            arguments = listOf(navArgument(Destination.Detail.ARG_ITEM_ID) {
                type = NavType.LongType
            }),
        ) { backStackEntry ->
            val savedStateHandle = backStackEntry.savedStateHandle
            // this will ALWAYS be present and not null
            val itemId = savedStateHandle.get<Long>(Destination.Detail.ARG_ITEM_ID)!!
            val detailViewModel = koinViewModel<DetailViewModel>(
                parameters = { org.koin.core.parameter.parametersOf(itemId) }
            )
            val detailScreenState by detailViewModel.state.collectAsStateWithLifecycle()
            DetailScreen(
                detailScreenState = detailScreenState,
                onRetryButtonClicked = { detailViewModel.handleIntent(DetailViewIntent.FetchDailyWeatherForecastById) },
                onBackButtonClicked = { navController.popBackStack() }
            )
        }
    }
}
