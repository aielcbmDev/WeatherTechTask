package com.charly.weatherapp.ui.mainscreen

import androidx.compose.runtime.Composable
import com.charly.weatherapp.ui.mainscreen.error.MainScreenError
import com.charly.weatherapp.ui.mainscreen.loading.MainScreenLoading
import com.charly.weatherapp.ui.mainscreen.success.MainScreenSuccess

@Composable
fun MainScreen(
    mainScreenState: MainScreenState,
    onDailyForecastModelClick: (Long) -> Unit,
    onRetryButtonClicked: () -> Unit
) {
    when (val uiState = mainScreenState.uiState) {
        is UiState.Loading -> MainScreenLoading()
        is UiState.Success -> MainScreenSuccess(
            dailyForecastModelList = uiState.dailyForecastModelList,
            onDailyForecastModelClick = onDailyForecastModelClick
        )

        is UiState.Error -> MainScreenError(onRetryButtonClicked)
    }
}
