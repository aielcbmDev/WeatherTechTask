package com.charly.weatherapp.ui.detailscreen

import androidx.compose.runtime.Composable
import com.charly.weatherapp.ui.common.error.ScreenError
import com.charly.weatherapp.ui.common.loading.ScreenLoading
import com.charly.weatherapp.ui.detailscreen.success.DetailScreenSuccess

@Composable
fun DetailScreen(
    detailScreenState: DetailScreenState,
    onRetryButtonClicked: () -> Unit,
    onBackButtonClicked: () -> Unit
) {
    when (val detailUiState = detailScreenState.detailUiState) {
        is DetailUiState.Loading -> ScreenLoading()
        is DetailUiState.Success -> DetailScreenSuccess(
            dailyForecastModel = detailUiState.dailyForecastModel,
            onBackButtonClicked = onBackButtonClicked
        )

        is DetailUiState.Error -> ScreenError(onRetryButtonClicked)
    }
}
